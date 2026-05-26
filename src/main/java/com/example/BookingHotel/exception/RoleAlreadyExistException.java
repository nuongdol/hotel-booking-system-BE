package com.example.BookingHotel.exception;

public class RoleAlreadyExistException extends RuntimeException {
    public RoleAlreadyExistException(String message){
        super(message);
    }
}
