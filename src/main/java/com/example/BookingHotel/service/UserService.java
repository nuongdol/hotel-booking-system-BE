package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.ResponseCode;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.exception.UserAlreadyExistsException;
import com.example.BookingHotel.model.Role;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.repository.RoleRepository;
import com.example.BookingHotel.repository.UserRepository;
import com.example.BookingHotel.request.MailBody;
import com.example.BookingHotel.request.ResetPasswordRequest;
import com.example.BookingHotel.request.UserRequest;
import com.example.BookingHotel.response.JwtResponse;
import com.example.BookingHotel.response.UserResponse;
import com.example.BookingHotel.security.User.HotelUserDetailsService;
import com.example.BookingHotel.security.jwt.JwtUtils;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final IEmailService emailService;
    private final RedisService redisService;
    private static final long OTP_EXPIRY_MINUTES = 10;
    private static final String OTP_PREFIX = "otp:forgot:";
    private static final String PRE_AUTHENTICATION_TOKEN_PREFIX = "preToken:";
    private static final long EXPIRY_MINUTES = 10;
    private final HotelUserDetailsService userDetailsService;
    private final JwtUtils jwtUtils;

    @Override
    public UserResponse registerUser(UserRequest user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        User userRegister = User.builder()
                .address(user.getAddress())
                .phone(user.getPhone())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(Collections.singletonList(userRole))
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(userRegister);
        return UserResponse.builder()
                .address(user.getAddress())
                .phone(user.getPhone())
                .email(user.getEmail())
                .lastName(user.getLastName())
                .firstName(userRegister.getFirstName())
                .roles(Collections.singletonList(userRole))
                .build();
    }

    public UserResponse registerAdmin(UserRequest admin) {
        if (userRepository.existsByEmail(admin.getEmail())) {
            throw new UserAlreadyExistsException(admin.getEmail() + "already exists");
        }
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        User adminRegister = User.builder()
                .address(admin.getAddress())
                .phone(admin.getPhone())
                .lastName(admin.getLastName())
                .firstName(admin.getFirstName())
                .email(admin.getEmail())
                .password(passwordEncoder.encode(admin.getPassword()))
                .roles(Collections.singletonList(adminRole))
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(adminRegister);
        return UserResponse.builder()
                .address(adminRegister.getAddress())
                .phone(adminRegister.getPhone())
                .email(adminRegister.getEmail())
                .lastName(adminRegister.getLastName())
                .firstName(adminRegister.getFirstName())
                .roles(Collections.singletonList(adminRole))
                .build();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if (theUser != null) {
            userRepository.deleteByEmail(email);
        }

    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new BusinessException(ResponseCode.USER_NOT_FOUND));
    }

    @Override
    public Page<User> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public String verifyEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new BusinessException(ResponseCode.USER_NOT_FOUND));
        String otp = otpGenerator();
        MailBody mailBody = MailBody.builder()
                .to(email)
                .text("This  the OTP for your forgot password request: " + otp)
                .subject("OTP for forgot password request")
                .build();
        //save otp in redis
        String keyOtp = OTP_PREFIX + email;
        redisService.setOtp(keyOtp, otp, OTP_EXPIRY_MINUTES, TimeUnit.MINUTES);
        //send otp for email
        try{
            emailService.sendSimpleMessage(mailBody);
        }catch (MessagingException e){
            throw new BusinessException(ResponseCode.SEND_EMAIL_FAILED);
        }
        return "successfully send email";
    }

    private String otpGenerator() {
        SecureRandom secureRandom = new SecureRandom();
        int otp = 1000000 + secureRandom.nextInt(900000);
        return String.valueOf(otp);
    }

    @Override
    public JwtResponse verifyOtp(String otp, String email, String preAuthenticationToken, HttpServletResponse httpResponse) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new BusinessException(ResponseCode.USER_NOT_FOUND));
        //verify preAuthToken
        String preAuthKey = PRE_AUTHENTICATION_TOKEN_PREFIX + email;
        String storePreAuthToken = redisService.getToken(preAuthKey);
        if(storePreAuthToken == null ||
        !storePreAuthToken.equals(preAuthenticationToken)){
            throw new BusinessException(ResponseCode.INVALID_PRE_AUTHENTICATION_TOKEN);
        }
        //verify otp
        String keyOtp = OTP_PREFIX + email;
        String storeOtp = redisService.getOtp(keyOtp);
        if(storeOtp == null){
            throw new BusinessException(ResponseCode.OTP_NOT_EXISTED_OR_VERIFIED);
        }
        if(storeOtp.equals(otp)){
            redisService.setVerifiedEmail("verified: " + email, "true", 15, TimeUnit.MINUTES);
        }
        redisService.delete(keyOtp);
        redisService.delete(preAuthKey);
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails,
                null, userDetails.getAuthorities());
        String accessToken = jwtUtils.generateJwtTokenForUser(authentication);
        String refreshToken = jwtUtils.generateJwtRefreshTokenForUser(authentication);
        //luu refreshToken vao cookie
        Cookie refreshTokenCookie = getRefreshTokenCookie(refreshToken);
        httpResponse.addCookie(refreshTokenCookie);
        List<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority).toList();
        return JwtResponse.builder()
                .accessToken(accessToken)
                .roles(roles)
                .email(email)
                .statusOtp(true)
                .build();
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

    @Override
    public void resetPassword(ResetPasswordRequest resetPasswordRequest) {
        //verified key
        String verifiedKey = "verified: " + resetPasswordRequest.getEmail();
        if (!redisService.hasOtp(verifiedKey)) {
            throw new BusinessException(ResponseCode.OTP_NOT_EXISTED_OR_VERIFIED);
        }
        User user = userRepository.findByEmail(resetPasswordRequest.getEmail())
                .orElseThrow(()-> new BusinessException(ResponseCode.USER_NOT_FOUND));
        user.setPassword(passwordEncoder.encode(resetPasswordRequest.getNewPassword()));
        userRepository.save(user);
        redisService.delete(verifiedKey);
    }
}