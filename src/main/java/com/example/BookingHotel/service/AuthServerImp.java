package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.ResponseCode;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.repository.UserRepository;
import com.example.BookingHotel.request.LoginRequest;
import com.example.BookingHotel.request.MailBody;
import com.example.BookingHotel.response.JwtResponse;
import com.example.BookingHotel.response.UserResponse;
import com.example.BookingHotel.security.User.HotelUserDetails;
import com.example.BookingHotel.security.jwt.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServerImp implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final RedisService redisService;
    private final UserRepository userRepository;
    private static final String OTP_PREFIX = "otp:forgot:";
    private static final String PRE_AUTHENTICATION_TOKEN_PREFIX = "preToken:";
    private static final long EXPIRY_MINUTES = 10;

    private final Executor emailExecutor;
    private final IEmailService emailService;


    @Override
    public JwtResponse login(LoginRequest request, HttpServletResponse response) {
        //lọc qua authenticationManager
        Authentication authentication =
                authenticationManager
                        .authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        String preAuthenticationToken = UUID.randomUUID().toString();
        String keyToken = PRE_AUTHENTICATION_TOKEN_PREFIX + request.getEmail();
        redisService.setToken(keyToken, preAuthenticationToken, EXPIRY_MINUTES, TimeUnit.MINUTES);
        //sinh mã OTP
        String otp = otpGenerator();
        //Lưu Otp vào trong redis
        String otpLoginKey = OTP_PREFIX + request.getEmail();
        redisService.setOtp(otpLoginKey, otp, EXPIRY_MINUTES, TimeUnit.MINUTES);
        //OTP duoc gui qua email
        emailExecutor.execute(() -> {
            MailBody mailBody = MailBody.builder()
                    .to(request.getEmail())
                    .text("This  the OTP for your forgot password request: " + otp)
                    .subject("OTP for forgot password request")
                    .build();
            try {
                emailService.sendSimpleMessage(mailBody);
            } catch (MessagingException e) {
                throw new BusinessException(ResponseCode.SEND_EMAIL_FAILED);
            }
        });
        HotelUserDetails userDetails = (HotelUserDetails) authentication.getPrincipal();
        return JwtResponse.builder()
                .preAuthenticationToken(preAuthenticationToken)
                .email(userDetails.getEmail()).build();
    }

    private String otpGenerator() {
        SecureRandom secureRandom = new SecureRandom();
        int otp = 1000000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
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
                UserResponse userResponse = new UserResponse();
                BeanUtils.copyProperties(user, userResponse);
                response.addCookie(refreshTokenCookie);
                jwtResponse = JwtResponse.builder()
                        .email(email)
                        .accessToken(accessToken)
                        .userResponse(userResponse)
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
