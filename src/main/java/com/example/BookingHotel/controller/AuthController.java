package com.example.BookingHotel.controller;

import com.example.BookingHotel.exception.UserAlreadyExistsException;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.request.LoginRequest;
import com.example.BookingHotel.response.ApiResponse;
import com.example.BookingHotel.response.JwtResponse;
import com.example.BookingHotel.request.UserRequest;
import com.example.BookingHotel.response.UserResponse;
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
    public ResponseEntity<ApiResponse<Object>> registerUser(@Valid @RequestBody UserRequest user) {
        try {
            UserResponse userRegister = userService.registerUser(user);
            log.info("User:{} {} register the hotel booking website", user.getFirstName(), user.getLastName());
            ApiResponse<Object> response = ApiResponse.builder()
                    .data(userRegister)
                    .code(HttpStatus.CREATED.value())
                    .message("Successful Registration!")
                    .build();
            return ResponseEntity.ok(response);
        } catch (UserAlreadyExistsException e) {
            ApiResponse<Object> response = ApiResponse.builder()
                    .code(HttpStatus.CONFLICT.value())
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse<Object>> registerAdmin(@Valid @RequestBody UserRequest admin) {
        try {
            UserResponse adminRegister = userService.registerAdmin(admin);
            log.info("Admin:{} {} register the hotel booking website", admin.getFirstName(), admin.getLastName());
            ApiResponse<Object> response = ApiResponse.builder()
                    .data(adminRegister)
                    .code(HttpStatus.CREATED.value())
                    .message("Successful Registration!")
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UsernameNotFoundException e) {
            ApiResponse<Object> response = ApiResponse.builder()
                    .code(HttpStatus.CONFLICT.value())
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
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
