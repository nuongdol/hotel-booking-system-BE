package com.example.BookingHotel.service;

import com.example.BookingHotel.model.BookedRoom;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface IPriceService {
     BigDecimal calculate(Long roomId, LocalDate checkinDate, LocalDate checkoutDate);
}
