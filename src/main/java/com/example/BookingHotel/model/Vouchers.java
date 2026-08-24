package com.example.BookingHotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vouchers")
@NoArgsConstructor
@AllArgsConstructor
public class Vouchers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long voucherId;

    @Column(name = "voucher_code")
    private String voucherCode;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    //1 promotion- n vouchers
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotions promotions;

    @Column(name = "quantity_limited")
    private Long quantityLimited;

    //n bookings - n vouchers
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "voucher_bookings",
            joinColumns = @JoinColumn(name = "voucher_id", referencedColumnName = "voucher_id"),
            inverseJoinColumns = @JoinColumn(name = "booking_id", referencedColumnName = "booking_id")
    )
    private Set<BookedRoom> bookedRooms = new HashSet<>();
}
