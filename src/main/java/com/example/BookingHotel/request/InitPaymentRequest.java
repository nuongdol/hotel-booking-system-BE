package com.example.BookingHotel.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class InitPaymentRequest {

    private String requestId;

    private String ipAddress;

    private Long customerId;

    private String txnRef;

    private Long amount;
}
