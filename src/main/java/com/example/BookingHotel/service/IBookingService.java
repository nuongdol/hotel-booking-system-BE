package com.example.BookingHotel.service;

import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.request.BookedRoomRequest;
import com.example.BookingHotel.response.BookingResponse;
import com.example.BookingHotel.response.InformationBookingRoom;

import java.time.LocalDateTime;
import java.util.List;
public interface IBookingService {
    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    String saveBooking(Long roomId, BookedRoomRequest bookingRequest);

    void cancelBooking(Long bookingId);

    List<BookedRoom> getAllBookings();

    List<BookedRoom> getBookingsByUserEmail(String email);

    BookingResponse markBooked(Long bookingId);

    String getBookingStatus(Long bookingId);

    InformationBookingRoom getInformationBookingRoom(Long roomId, String city, LocalDateTime checkInDate,
                                                     Integer totalNights, Integer adults, Integer children);
}
