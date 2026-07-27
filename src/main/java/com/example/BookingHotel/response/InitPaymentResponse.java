package com.example.BookingHotel.response;

import com.example.BookingHotel.model.BookedRoom;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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
