package com.example.BookingHotel.exception;

public class InvalidPaymentRequestException extends RuntimeException{
    public InvalidPaymentRequestException(String message){
        super(message);
    }
}
