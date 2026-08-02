package com.example.BookingHotel.controller;

import com.example.BookingHotel.exception.UserAlreadyExistsException;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.request.LoginRequest;
import com.example.BookingHotel.response.JwtResponse;
import com.example.BookingHotel.service.IAuthService;
import com.example.BookingHotel.service.IUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final IUserService userService;
    private final IAuthService authService;


    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            userService.registerUser(user);
            return ResponseEntity.ok("Registration successful!");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestBody User Admin) {
        try {
            userService.registerAdmin(Admin);
            return ResponseEntity.ok("Registration successful!");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest request,
                                              HttpServletResponse response) {
        try {
            log.info("User login with email:{}", request.getEmail());
            JwtResponse userResponse = authService.login(request, response);
            //add access token vao header
            response.setHeader("Authorization", "Bearer "
            + userResponse.getAccessToken());
            return ResponseEntity.ok(userResponse);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
