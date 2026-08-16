package com.example.BookingHotel.service;

import com.example.BookingHotel.model.User;
import com.example.BookingHotel.request.UserRequest;
import com.example.BookingHotel.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface IUserService {
    UserResponse registerUser(UserRequest user);
    UserResponse registerAdmin(UserRequest admin);
    void deleteUser(String email);
    User getUser(String email);

    Page<User> getUsers(Pageable pageable);
}