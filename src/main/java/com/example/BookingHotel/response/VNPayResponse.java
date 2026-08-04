package com.example.BookingHotel.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VNPayResponse {
    private String code;
    private String message;
    private String paymentUrl;
}
