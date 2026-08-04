package com.example.BookingHotel.controller;

import com.example.BookingHotel.exception.UserAlreadyExistsException;
import com.example.BookingHotel.request.LoginRequest;
import com.example.BookingHotel.request.UserRequest;
import com.example.BookingHotel.response.ApiResponse;
import com.example.BookingHotel.response.JwtResponse;
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
    public ResponseEntity<ApiResponse<UserResponse>> registerUser(@Valid @RequestBody UserRequest user) {
        try {
            UserResponse userRegister = userService.registerUser(user);
            log.info("User:{} {} register the hotel booking website", user.getFirstName(), user.getLastName());
            ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                    .status("SUCCESS")
                    .data(userRegister)
                    .code(HttpStatus.CREATED.value())
                    .message("Successful Registration!")
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UserAlreadyExistsException e) {
            ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                    .status("ERROR")
                    .code(HttpStatus.CONFLICT.value())
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/register-admin")
    public ResponseEntity<ApiResponse<UserResponse>> registerAdmin(@Valid @RequestBody UserRequest admin) {
        try {
            UserResponse adminRegister = userService.registerAdmin(admin);
            log.info("Admin:{} {} register the hotel booking website", admin.getFirstName(), admin.getLastName());
            ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                    .data(adminRegister)
                    .code(HttpStatus.CREATED.value())
                    .message("Successful Registration!")
                    .build();
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (UsernameNotFoundException e) {
            ApiResponse<UserResponse> response = ApiResponse.<UserResponse>builder()
                    .code(HttpStatus.CONFLICT.value())
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<JwtResponse>> authenticateUser(@Valid @RequestBody LoginRequest request,
                                              HttpServletResponse response) {
        try {
            log.info("User login with email:{}", request.getEmail());
            JwtResponse userResponse = authService.login(request, response);
            //add access token vao header
            response.setHeader("Authorization", "Bearer "
                    + userResponse.getAccessToken());
            ApiResponse<JwtResponse> apiResponse = ApiResponse.<JwtResponse>builder()
                    .status("SUCCESS")
                    .data(userResponse)
                    .code(200)
                    .message("Login successful!")
                    .build();
            return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
        } catch (UsernameNotFoundException e) {
            ApiResponse<JwtResponse> apiResponse = ApiResponse.<JwtResponse>builder()
                    .status("ERROR")
                    .code(HttpStatus.CONFLICT.value())
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(apiResponse);
        }
    }
}
