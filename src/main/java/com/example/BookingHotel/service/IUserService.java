package com.example.BookingHotel.service;

import com.example.BookingHotel.model.User;

import java.util.List;



public interface IUserService {
    User registerUser(User user);
    User registerAdmin(User Admin);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);

}