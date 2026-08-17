package com.example.BookingHotel.controller;

import com.example.BookingHotel.request.ResetPasswordRequest;
import com.example.BookingHotel.response.ApiResponse;
import com.example.BookingHotel.response.ResetPasswordResponse;
import com.example.BookingHotel.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/password")
@RequiredArgsConstructor
public class ForgotPasswordController {

    private final UserService userService;

    //send mail verification
    @PostMapping("/{email}")
    @Operation(description = "send mail for OTP")
    public ResponseEntity<ApiResponse<String>> verifyEmail(
            @PathVariable @Email(message = "Email khong dung dinh dang") String email) {
        String verifyEmailStatus = userService.verifyEmail(email);
        ApiResponse<String> response = ApiResponse.<String>builder()
                .status("SUCCESS")
                .data(verifyEmailStatus)
                .code(HttpStatus.CREATED.value())
                .message("Successful verify email!")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    //verify OTP
    @PostMapping("/{opt}/{email}")
    @Operation(description = "verify Otp")
    public ResponseEntity<ApiResponse<String>> verifyOtp(@PathVariable String otp,
                                                         @PathVariable @Email(message = "Email khong dung dinh dang") String email) {
        boolean validOtp = userService.verifyOtp(otp, email);

        ApiResponse<String> response = ApiResponse.<String>builder()
                .status("SUCCESS")
                .data(validOtp ? "success valid otp" : "failed valid otp")
                .code(HttpStatus.CREATED.value())
                .message("Successful verify email!")
                .build();
        return ResponseEntity.ok(response);
    }

    //reset-password
    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<ResetPasswordResponse>> resetPassword(@Valid @RequestBody ResetPasswordRequest resetPasswordRequest) {
        userService.resetPassword(resetPasswordRequest);
        ApiResponse<ResetPasswordResponse> response = ApiResponse
                .<ResetPasswordResponse>builder()
                .status("SUCCESS")
                .message("Successful reset password")
                .code(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
}
