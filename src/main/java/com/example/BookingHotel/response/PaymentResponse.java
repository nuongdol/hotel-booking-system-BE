package com.example.BookingHotel.response;


import com.example.BookingHotel.model.BookedRoom;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class PaymentResponse {
    private Long id;
    private BookedRoom booking;
    private String userEmail;
    private String paymentMethod;
    private BigDecimal paymentAccount;
    private String paymentStatus;
    private LocalDateTime timestamp;
}
