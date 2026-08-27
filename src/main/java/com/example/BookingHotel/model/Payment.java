package com.example.BookingHotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Table(name = "payment")
@DynamicInsert
@DynamicUpdate
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long paymentId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name="booking_id", referencedColumnName = "booking_id")
    private BookedRoom booking;

    @Column(name="user_email")
    private String userEmail;

    @Column(name="payment_method")
    private String paymentMethod;

    @Column(name="payment_account")
    private BigDecimal paymentAccount;

    @Column(name="payment_status")
    private String paymentStatus;

    @Column(name="time_stamp")
    private LocalDateTime timestamp;

    public Payment(Long id, String paymentMethod){
        this.paymentId = id;
        this.paymentMethod = paymentMethod;
    }

}
