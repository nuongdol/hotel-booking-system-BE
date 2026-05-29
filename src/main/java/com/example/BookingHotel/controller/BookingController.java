package com.example.BookingHotel.controller;


import com.example.BookingHotel.exception.InvalidBookingRequestException;
import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.model.Room;
import com.example.BookingHotel.response.ApiResponse;
import com.example.BookingHotel.response.BookingResponse;
import com.example.BookingHotel.response.HoleRoom;
import com.example.BookingHotel.response.RoomResponse;
import com.example.BookingHotel.service.IBookingService;
import com.example.BookingHotel.service.IRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.module.ResolutionException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
//@CrossOrigin("http://localhost:5173")
@RequestMapping("/api/v1/bookings/")
public class BookingController {
    private final IBookingService bookingService;
    private final IRoomService roomService;

    @GetMapping("/all-bookings")
    public ResponseEntity<List<BookingResponse>> getAllBookings() {
        List<BookedRoom> bookings = bookingService.getAllBookings();
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedRoom booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    @GetMapping("/confirmation/{confirmationCode}")
    public ResponseEntity<?> getBookingConfirmationCode(@PathVariable("confirmationCode") String confirmationCode) {
        try {
            BookedRoom booking = bookingService.findByBookingConfirmationCode(confirmationCode);
            BookingResponse bookingResponse = getBookingResponse(booking);
            return ResponseEntity.ok(bookingResponse);
        } catch (ResolutionException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    //luu thong tin dat phong
    @PostMapping("/room/{roomId}/booking")
    public ResponseEntity<?> saveBooking(@PathVariable("roomId") Long roomId, @RequestBody BookedRoom bookingRequest) {
        try {
            String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
            return ResponseEntity.ok("Room booked successfully, Your booking confirmation code is:"
                    + confirmationCode);
        } catch (InvalidBookingRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/booking/{bookingId}/delete")
    public void cancelBooking(@PathVariable("bookingId") Long bookingId) {
        bookingService.cancelBooking(bookingId);

    }

    @GetMapping("/user/{userId}/bookings")
    public ResponseEntity<List<BookingResponse>> getBookingByUserEmail(@PathVariable("userId") String email) {
        List<BookedRoom> bookings = bookingService.getBookingsByUserEmail(email);
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedRoom booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    //hold room before payment
    @PostMapping("/{roomId}/holds")
    public ResponseEntity<ApiResponse<HoleRoom>> holdRoom(@PathVariable(name = "roomId") Long roomId,
                                                          @RequestParam(name = "userId") Long userId) {
        try {
            HoleRoom response = bookingService.holdRoom(roomId, userId);
            return new ResponseEntity<>(new ApiResponse<>("success", 200, "Created", response), HttpStatus.CREATED);
        } catch (Exception e) {
            ApiResponse<HoleRoom> errorResponse = new ApiResponse<>("error", 400, e.getMessage(), null);
            return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
        }
    }

    private BookingResponse getBookingResponse(BookedRoom booking) {
        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
        RoomResponse room = new RoomResponse(theRoom.getId(),
                theRoom.getRoomType(),
                theRoom.getRoomPrice());
        return new BookingResponse(booking.getBookingId(), booking.getCheckInDate(), booking.getCheckOutDate(),
                booking.getGuestFullName(), booking.getGuestEmail(), booking.getNumOfAdults(), booking.getNumOfChildren(), booking.getTotalNumOfGuest(), booking.getBookingConfirmationCode(), room);
    }

}
