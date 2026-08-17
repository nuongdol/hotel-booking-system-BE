package com.example.BookingHotel.service;

import com.example.BookingHotel.model.User;
import com.example.BookingHotel.request.ResetPasswordRequest;
import com.example.BookingHotel.request.UserRequest;
import com.example.BookingHotel.response.ResetPasswordResponse;
import com.example.BookingHotel.response.UserResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IUserService {
    UserResponse registerUser(UserRequest user);
    UserResponse registerAdmin(UserRequest admin);
    void deleteUser(String email);
    User getUser(String email);

    Page<User> getUsers(Pageable pageable);

    String verifyEmail(@Email(message = "Email khong dung dinh dang") String email);

    boolean verifyOtp(String otp, @Email(message = "Email khong dung dinh dang") String email);

    void resetPassword(@Valid ResetPasswordRequest resetPasswordRequest);
}