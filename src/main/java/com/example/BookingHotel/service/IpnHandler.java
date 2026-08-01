package com.example.BookingHotel.service;

import com.example.BookingHotel.response.IpnResponse;

import java.util.Map;

public interface IpnHandler {
    IpnResponse process(Map<String, String> params);
}
