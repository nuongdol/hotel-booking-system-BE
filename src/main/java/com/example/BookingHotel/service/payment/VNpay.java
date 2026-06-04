package com.example.BookingHotel.service.payment;

import com.example.BookingHotel.annotation.PaymentType;
import org.springframework.stereotype.Component;

@Component
@PaymentType("VN_PAY")
public class VNpay implements PaymentStrategy{
    @Override
    public void pay(double amount) {

    }
}
