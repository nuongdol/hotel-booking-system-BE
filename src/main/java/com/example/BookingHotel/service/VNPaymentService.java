package com.example.BookingHotel.service;

import com.example.BookingHotel.request.InitPaymentRequest;
import com.example.BookingHotel.response.InitPaymentResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class VNPaymentService implements IPaymentService{
    @Override
    public InitPaymentResponse init(InitPaymentRequest initPaymentRequest) {
        return null;
    }
}
