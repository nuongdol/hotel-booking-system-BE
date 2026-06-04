package com.example.BookingHotel.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VNPayResponse {
    private String code;
    private String message;
    private String paymentUrl;
}
