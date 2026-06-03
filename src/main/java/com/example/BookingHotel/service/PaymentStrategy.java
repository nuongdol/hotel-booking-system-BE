package com.example.BookingHotel.service;

public interface PaymentStrategy {
    void pay(double amount);
    String getPaymentMethod();
}
