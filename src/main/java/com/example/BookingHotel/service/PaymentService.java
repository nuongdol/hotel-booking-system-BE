package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.ErrorCode;
import com.example.BookingHotel.annotation.PaymentType;
import com.example.BookingHotel.exception.BaseHotelException;
import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.model.Payment;
import com.example.BookingHotel.repository.PaymentRepository;
import com.example.BookingHotel.service.payment.PaymentStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.math.BigDecimal;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {

    private final Map<String, PaymentStrategy> strategies;
    private final PaymentRepository paymentRepository;

    @Autowired
    public PaymentService(List<PaymentStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(
                        strategy ->
                                strategy.getClass().getAnnotation(PaymentType.class).value(),
                        Function.identity()
                ));
        this.paymentRepository = null;
    }

    public void processPayment(String strategyName, double amount) {
        PaymentStrategy strategy = strategies.get(strategyName);
        if (strategy == null) {
            throw new BaseHotelException(ErrorCode.PAYMENT_PROCESSING);
        }
        strategy.pay(amount);
    }

    @Override
    public Payment postPayment(Long id, BookedRoom bookedRoom, String userEmail,
                               String paymentMethod, BigDecimal paymentAccount,
                               String paymentStatus, LocalDateTime timestamp) {
        Payment payment = new Payment(id, bookedRoom, userEmail, paymentMethod, paymentAccount
                , paymentStatus, timestamp);
        return paymentRepository.save(payment);
    }

    @Override
    public Payment updatePayment(Long id, String userEmail, String paymentMethod,
                                 BigDecimal paymentAccount, String paymentStatus,
                                 LocalDateTime timestamp) {
        Optional<Payment> payment = paymentRepository.findById(id);
        Payment payment1 = payment.get();
        if (userEmail != null) {
            payment1.setUserEmail(userEmail);
        }
        if (paymentMethod != null) {
            payment1.setPaymentMethod(paymentMethod);
        }
        if (paymentAccount != null) {
            payment1.setPaymentAccount(paymentAccount);
        }
        if (paymentStatus != null) {
            payment1.setPaymentStatus(paymentStatus);
        }
        if (timestamp != null) {
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
        if (payment.isPresent()) {
            paymentRepository.deleteById(idPayment);
        }
    }
}
