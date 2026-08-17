package com.example.BookingHotel.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotNull(message = "email cannot be null")
    @Email(message = "email must be valid")
    private String email;

    @NotNull(message = "password cannot be null")
    private String newPassword;
}
