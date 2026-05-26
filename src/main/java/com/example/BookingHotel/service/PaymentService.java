package com.example.BookingHotel.service;

import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.model.Payment;
import com.example.BookingHotel.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService{
    private final PaymentRepository paymentRepository;

    @Override
    public Payment postPayment(Long id, BookedRoom bookedRoom, String userEmail,
                               String paymentMethod, BigDecimal paymentAccount,
                               String paymentStatus, LocalDateTime timestamp) {
        Payment payment = new Payment(id, bookedRoom, userEmail, paymentMethod,paymentAccount
        ,paymentStatus, timestamp);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(Long id, String userEmail, String paymentMethod,
                                 BigDecimal paymentAccount, String paymentStatus,
                                 LocalDateTime timestamp) {
        Optional<Payment> payment = paymentRepository.findById(id);
        Payment payment1 = payment.get();
        if(userEmail != null){
            payment1.setUserEmail(userEmail);
        }
        if(paymentMethod != null){
            payment1.setPaymentMethod(paymentMethod);
        }
        if(paymentAccount != null){
            payment1.setPaymentAccount(paymentAccount);
        }
        if(paymentStatus != null){
            payment1.setPaymentStatus(paymentStatus);
        }
        if(timestamp != null){
            payment1.setTimestamp(timestamp);

        }

        return paymentRepository.save(payment1);
    }

    @Override
    public Optional<Payment> getPayment(Long id) {
        return paymentRepository.findById(id);
    }

    @Override
    public void deletePayment(Long idPayment) {
        Optional<Payment> payment = paymentRepository.findById(idPayment);
        if(payment.isPresent()){
            paymentRepository.deleteById(idPayment);
        }
    }
}
