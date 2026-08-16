package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.ResponseCode;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.repository.UserRepository;
import com.example.BookingHotel.request.LoginRequest;
import com.example.BookingHotel.response.JwtResponse;
import com.example.BookingHotel.security.User.HotelUserDetails;
import com.example.BookingHotel.security.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServerImp implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RedisService redisService;
    private final UserRepository userRepository;

    @Override
    public JwtResponse login(LoginRequest request, HttpServletResponse response) {
        //lọc qua authenticationManager
        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String accessToken = jwtUtils.generateJwtTokenForUser(authentication);
        String refreshToken = jwtUtils.generateJwtRefreshTokenForUser(authentication);
        //luu refreshToken vao cookie
        Cookie refreshTokenCookie = getRefreshTokenCookie(refreshToken);
        response.addCookie(refreshTokenCookie);
        HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();
        return JwtResponse.builder()
                .accessToken(accessToken)
                .roles(roles)
                .email(userDetails.getEmail()).build();
    }

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        //check token null
        String accessTokenBearer = request.getHeader("Authorization");
        if (accessTokenBearer == null || !accessTokenBearer.startsWith("Bearer ")) {
            throw new BusinessException(ResponseCode.INVALID_AUTHORIZATION_HEADER);
        }
        String accessToken = request.getHeader("Authorization").substring(7);
        var cookieRequest = request.getCookies();
        if (cookieRequest == null) {
            throw new BusinessException(ResponseCode.REFRESH_TOKEN_NOT_FOUND);
        }
        Cookie refreshTokenCookie = Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refresh_token"))
                .findFirst().orElseThrow(
                        () -> new BusinessException(ResponseCode.REFRESH_TOKEN_NOT_FOUND));
        log.info("Token value from request: {}", accessToken);
        invalidateToken(accessToken);
        invalidateToken(refreshTokenCookie.getValue());
        invalidateRefreshTokenCookie(response, refreshTokenCookie);
    }

    @Override
    public JwtResponse refreshToken(HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            throw new BusinessException(ResponseCode.REFRESH_TOKEN_NOT_FOUND);
        }
        var refreshTokenCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("refresh_token"))
                .findFirst().orElseThrow(() ->
                        new BusinessException(ResponseCode.REFRESH_TOKEN_NOT_FOUND));

        var currentRefreshToken = refreshTokenCookie.getValue();
        if (redisService.hasToken(currentRefreshToken)) {
            return null;
        }
        JwtResponse jwtResponse = new JwtResponse();
        invalidateToken(currentRefreshToken);
        if (currentRefreshToken != null) {
            if (!jwtUtils.isTokenValid(currentRefreshToken)) {
                var email = jwtUtils.extractEmail(currentRefreshToken);
                User user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new BusinessException(ResponseCode.USER_NOT_FOUND));
                //loc qua authentication
                Authentication authentication =
                        authenticationManager
                                .authenticate(new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));
                String accessToken = jwtUtils.generateJwtTokenForUser(authentication);
                String newRefreshToken = jwtUtils.generateJwtRefreshTokenForUser(authentication);
                refreshTokenCookie = getRefreshTokenCookie(newRefreshToken);
                response.addCookie(refreshTokenCookie);
                jwtResponse = JwtResponse.builder()
                        .email(email)
                        .accessToken(accessToken)
                        .build();
            }
        }
        return jwtResponse;
    }

    private void invalidateRefreshTokenCookie(HttpServletResponse response, Cookie refreshTokenCookie) {
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
    }

    private void invalidateToken(String token) {
        long expirationTime = (jwtUtils.extractExpiration(token).getTime() - System.currentTimeMillis()) / 1000;
        log.info("Invalidating token with remaining: TTL: {} seconds", expirationTime);
        /*
        blackList neu token chua expired token het han roi thi
        khong can nua
         */
        if (expirationTime > 0L) {
            log.info("New Access Token generated successfully, invalidating previous refresh token");
            redisService.setToken(token, "blacklisted", expirationTime, TimeUnit.SECONDS);
        }
    }

    private Cookie getRefreshTokenCookie(String refreshToken) {
        //tách sợ tràn số, sai kết quả
        var cookieMaxSeconds = (jwtUtils.extractExpiration(refreshToken).getTime() - System.currentTimeMillis()) / 1000;
        int cookieMaxAge = (int) Math.max(cookieMaxSeconds, 0);
        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken);
        refreshTokenCookie.setHttpOnly(true);//prevents javascript access (XSS protection)
        refreshTokenCookie.setSecure(true); //ensure https only (important for production
        refreshTokenCookie.setPath("/");//available for the entire application
        refreshTokenCookie.setMaxAge(cookieMaxAge);//set to remaining TTL of the token
        return refreshTokenCookie;
    }
}
