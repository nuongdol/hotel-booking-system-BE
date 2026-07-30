package com.example.BookingHotel.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseCode {
    SUCCESS("SUCCESS", 200),
    CHECKIN_DATE_INVALID("CHECKIN_DATE_INVALID",400),
    TOTAL_NUMBER_GUEST_INVALID("TOTAL_NUMBER_GUEST_INVALID",400),
    ERROR_SYSTEM("ERROR_SYSTEM", 400),
    ROOM_EMPTY_INVALID("ROOM_EMPTY_INVALID", 400),
    BOOKED_ROOM_INVALID("BOOKED_ROOM_INVALID", 400),
    CONFLICTED_ROOM("CONFLICT_ROOM", 400),
    ROOM_NOT_FOUND("ROOM_NOT_CONFLICT", 400),
    VNPAY_SIGNIN_FAILED("VNPAY_SIGNIN_FAILED", 400);


    private final String message;
    private final Integer code;
}
