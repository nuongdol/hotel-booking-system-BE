package com.example.BookingHotel.service;

import com.example.BookingHotel.request.LoginRequest;
import com.example.BookingHotel.response.JwtResponse;
import com.example.BookingHotel.security.User.HotelUserDetails;
import com.example.BookingHotel.security.jwt.JwtUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthServerImp implements IAuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

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
