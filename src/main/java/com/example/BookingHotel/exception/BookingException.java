package com.example.BookingHotel.exception;



import org.springframework.http.HttpStatus;

public class BookingException extends BaseHotelException{

    private final Long bookingId;

    public BookingException(String message, String errorCode, HttpStatus status, Long bookingId) {
        super(message, errorCode, status);
        this.bookingId = bookingId;
    }
    public BookingException(String message, String errorCode, HttpStatus status){
        super(message, errorCode, status);
        this.bookingId = null;
    }

}
