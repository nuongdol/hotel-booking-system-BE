package com.example.BookingHotel.response;

import com.example.BookingHotel.model.BookedRoom;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class InitPaymentResponse {

    private Long id;
    private BookedRoom booking;
    private String userEmail;
    private String paymentMethod;
    private BigDecimal paymentAccount;
    private String paymentStatus;
    private LocalDateTime timestamp;
    private String vnpUrl;
}
