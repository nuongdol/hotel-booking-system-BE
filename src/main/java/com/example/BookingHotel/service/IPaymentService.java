package com.example.BookingHotel.service;


import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.model.Payment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

public interface IPaymentService {
    Payment postPayment(Long id, BookedRoom bookedRoom, String userEmail
    , String paymentMethod, BigDecimal paymentAccount,String paymentStatus, LocalDateTime timestamp);
    Payment updatePayment(Long id, String userEmail, String paymentMethod,
                          BigDecimal paymentAccount, String paymentStatus, LocalDateTime timestamp);
    Optional<Payment> getPayment(Long idPayment);

    void deletePayment(Long idPayment);


}
