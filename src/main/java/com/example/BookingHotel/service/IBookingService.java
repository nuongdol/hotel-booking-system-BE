package com.example.BookingHotel.service;

import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.response.BookingResponse;

import java.util.List;
public interface IBookingService {
    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    String saveBooking(Long roomId, BookedRoom bookingRequest);

    void cancelBooking(Long bookingId);

    List<BookedRoom> getAllBookings();

    List<BookedRoom> getBookingsByUserEmail(String email);

    BookingResponse markBooked(Long bookingId);

    String getBookingStatus(Long bookingId);
}
