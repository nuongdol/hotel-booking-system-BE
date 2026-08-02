package com.example.BookingHotel.service;

import com.example.BookingHotel.request.LoginRequest;
import com.example.BookingHotel.response.JwtResponse;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

public interface IAuthService {

    JwtResponse login(@Valid LoginRequest request,
                      HttpServletResponse response);
}
