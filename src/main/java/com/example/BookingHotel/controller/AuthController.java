package com.example.BookingHotel.controller;

import com.example.BookingHotel.exception.UserAlreadyExistsException;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.request.LoginRequest;
import com.example.BookingHotel.response.ApiResponse;
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
    public ResponseEntity<?> registerUser(@Valid @RequestBody User user) {
        try {
            log.info("User:{} {} register the hotel booking website", user.getFirstName(), user.getLastName());
            User userRegister = userService.registerUser(user);
            ApiResponse<Object> response = ApiResponse.builder()
                    .data(userRegister)
                    .code(200)
                    .message("Successful Registration!")
                    .build();
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@Valid @RequestBody User admin) {
        try {
            log.info("Admin:{} {} register the hotel booking website", admin.getFirstName(), admin.getLastName());
            User adminRegister = userService.registerAdmin(admin);
            ApiResponse<Object> response = ApiResponse.builder()
                    .data(adminRegister)
                    .code(200)
                    .message("Successful Registration!")
                    .build();
            return ResponseEntity.ok(response);
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
            ApiResponse<Object> apiResponse = ApiResponse.builder()
                    .data(userResponse)
                    .code(200)
                    .message("Successful Registration!")
                    .build();
            return ResponseEntity.ok(apiResponse);
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
