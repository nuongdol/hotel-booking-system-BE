package com.example.BookingHotel.repository;

import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


import java.util.Optional;
@EnableJpaRepositories
@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByBooking(BookedRoom booking);
    Optional<Payment> findByUserEmail(String email);

    void deleteById(Long idPayment);
}
