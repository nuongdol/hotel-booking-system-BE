package com.example.BookingHotel.controller;


import com.example.BookingHotel.exception.InvalidBookingRequestException;
import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.model.Room;
import com.example.BookingHotel.response.ApiResponse;
import com.example.BookingHotel.response.BookingResponse;
import com.example.BookingHotel.response.InformationBookingRoom;
import com.example.BookingHotel.response.RoomResponse;
import com.example.BookingHotel.service.IBookingService;
import com.example.BookingHotel.service.IRoomService;
import com.example.BookingHotel.util.RequestUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.module.ResolutionException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/booking")
public class BookingController {

    private final IBookingService bookingService;
    private final IRoomService roomService;

    @GetMapping()
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
    @PostMapping("/{roomId}")
    public ResponseEntity<?> saveBooking(@PathVariable("roomId") Long roomId,
                                         @RequestBody BookedRoom bookingRequest,
                                         HttpServletRequest httpServletRequest) {
        String ipAddress = RequestUtil.getIpAddress(httpServletRequest);
        bookingRequest.setIpAddress(ipAddress);
        try {
            log.info("Booking Request: {}", bookingRequest);
            String confirmationCode = bookingService.saveBooking(roomId, bookingRequest);
            return ResponseEntity.ok("Room booked successfully, Your booking confirmation code is:"
                    + confirmationCode);
        } catch (InvalidBookingRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //checkout summary(lay thong tin tong quan cua nguoi dat phong tron session)
    @GetMapping("/checkout-summary/{roomId}")
    @Operation(description = "lay thong tin tong quan cua nguoi dat phong tron session")
    public ResponseEntity<ApiResponse<InformationBookingRoom>> checkSummary(@PathVariable("roomId") Long roomId) {
        InformationBookingRoom informationBookingRoom = bookingService.getInformationBookingRoom(roomId);
        ApiResponse<InformationBookingRoom> apiResponse = ApiResponse.<InformationBookingRoom>builder()
                .message("Get information booking room of user")
                .code(HttpStatus.OK.value())
                .data(informationBookingRoom)
                .status("200")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    @DeleteMapping("/{bookingId}")
    public void cancelBooking(@PathVariable("bookingId") Long bookingId) {
        bookingService.cancelBooking(bookingId);

    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponse>> getBookingByUserEmail(@PathVariable("userId") String email) {
        List<BookedRoom> bookings = bookingService.getBookingsByUserEmail(email);
        List<BookingResponse> bookingResponses = new ArrayList<>();
        for (BookedRoom booking : bookings) {
            BookingResponse bookingResponse = getBookingResponse(booking);
            bookingResponses.add(bookingResponse);
        }
        return ResponseEntity.ok(bookingResponses);
    }

    private BookingResponse getBookingResponse(BookedRoom booking) {
        Room theRoom = roomService.getRoomById(booking.getRoom().getId()).get();
        RoomResponse room = new RoomResponse(theRoom.getId(),
                theRoom.getRoomType(),
                theRoom.getRoomPrice());
        return BookingResponse.builder()
                .bookingId(booking.getBookingId())
                .bookingConfirmationCode(booking.getBookingConfirmationCode())
                .checkInDate(booking.getCheckInDate())
                .checkOutDate(booking.getCheckOutDate())
                .guestEmail(booking.getGuestEmail())
                .NumOfAdults(booking.getNumOfAdults())
                .guestFullName(booking.getGuestFullName())
                .totalNumOfGuest(booking.getTotalNumOfGuest())
                .NumOfChildren(booking.getNumOfChildren())
                .status(booking.getStatus())
                .room(room)
                .build();
    }

    @GetMapping("/{bookingId}/status")
    ResponseEntity<String> getBookingStatus(@PathVariable Long bookingId) {
        try {
            String response = bookingService.getBookingStatus(bookingId);
            return ResponseEntity.ok(response);
        } catch (InvalidBookingRequestException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
