package com.example.BookingHotel.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    //trường ko thể bỏ trống nó ko thể là notnull, chuỗi trống,
    // chỉ có thể là khoảng trắng
    private String email;

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(min = 8, message = "Mật khẩu phải ít nhất 8 ký tự")
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d).+$",
            message = "Mật khẩu phải chứa ít nhất 1 chữ cái và 1 chữ số"
    )
    private String password;
}



