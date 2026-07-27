package com.example.BookingHotel.service;



import com.example.BookingHotel.request.InitPaymentRequest;
import com.example.BookingHotel.response.InitPaymentResponse;


public interface IPaymentService {
     InitPaymentResponse init(InitPaymentRequest initPaymentRequest);
}
