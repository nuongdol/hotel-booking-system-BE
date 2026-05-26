package com.example.BookingHotel.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {
    @NotBlank//trường ko thể bỏ trống nó ko thể là notnull, chuỗi trống,
    // chỉ có thể là khoảng trắng
    private String email;
    @NotBlank
    private String password;
}



