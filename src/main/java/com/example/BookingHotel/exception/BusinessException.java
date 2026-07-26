package com.example.BookingHotel.exception;

import com.example.BookingHotel.constant.ResponseCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BusinessException extends RuntimeException{
    ResponseCode responseCode;

    public BusinessException(ResponseCode responseCode, String message){
        super(message);
        this.responseCode = responseCode;
    }
}
