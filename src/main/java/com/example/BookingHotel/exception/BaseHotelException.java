package com.example.BookingHotel.exception;


import com.example.BookingHotel.Enum.ErrorCode;


public class BaseHotelException extends RuntimeException{
    private ErrorCode errorCode;

    public BaseHotelException(ErrorCode errorCode){
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
