package com.example.BookingHotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "room_rate_plans")
@NoArgsConstructor
@AllArgsConstructor
public class RoomRatePlans {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rate_plan_id")
    private Long ratePlanId;
    //FK
    //room_type_id
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "original_price")
    private BigDecimal originalPrice;

    @Column(name = "discount_percent")
    private Float discountPercent;

    @Column(name = "is_breakfast_included")
    private Boolean isBreakfastIncluded;

    @Column(name = "payment_at_hotel")
    private String paymentAtHotel;

    @Column(name = "currency")
    private String currency;

    @OneToMany(mappedBy = "roomRatePlans", cascade = CascadeType.ALL)
    private List<Policies> policies;

    @OneToMany(mappedBy = "roomRatePlans", cascade = CascadeType.ALL)
    private List<BookedRoom> bookedRooms;

    @Column(name = "is_tax_included")
    private Boolean isTaxIncluded;

    @OneToMany(mappedBy = "roomRatePlans", cascade = CascadeType.ALL)
    private List<Promotions> promotions;
}
