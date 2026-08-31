package com.example.BookingHotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vouchers")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class Vouchers {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "voucher_id")
    private Long voucherId;

    @Column(name = "voucher_code")
    private String voucherCode;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "description")
    private String description;

    //1 promotion- n vouchers
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "promotion_id")
    private Promotions promotions;

    //n bookings - n vouchers
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "voucher_bookings",
            joinColumns = @JoinColumn(name = "voucher_id", referencedColumnName = "voucher_id"),
            inverseJoinColumns = @JoinColumn(name = "booking_id", referencedColumnName = "booking_id")
    )
    private Set<BookedRoom> bookedRooms = new HashSet<>();

    @Column(name = "used_voucher_count")
    private Integer usedVoucherCount;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /*
    1 = active
    0 = non-active
     */
    @Column(name = "status")
    private Integer status;

    @Column(name = "ended_at")
    private LocalDateTime endedAt;

    @ManyToMany(mappedBy = "vouchers", cascade = CascadeType.ALL)
    private Collection<Badges> badges;
}
