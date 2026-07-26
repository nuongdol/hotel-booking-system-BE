package com.example.BookingHotel.service;

import java.time.LocalDate;

public interface IRoomInventoryService {

    int processBookingRoom(Long roomId, LocalDate date);
}
