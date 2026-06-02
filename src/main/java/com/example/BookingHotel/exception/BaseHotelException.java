package com.example.BookingHotel.exception;


import org.springframework.http.HttpStatus;


public class BaseHotelException extends RuntimeException{
    private String errorCode;
    private HttpStatus status;

    public BaseHotelException(String message, String errorCode, HttpStatus status){
        super(message);
        this.errorCode = errorCode;
        this.status = status;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
