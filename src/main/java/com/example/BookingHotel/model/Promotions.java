package com.example.BookingHotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
@AllArgsConstructor
@NoArgsConstructor
public class Promotions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "promo_id")
    private Long promoId;

    @Column(name = "label")
    private String label;

    @Column(name = "discount_type")
    private String discountType;

    @Column(name = "discount_value")
    private float discountValue;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    //FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_rate_plan_id")
    private RoomRatePlans roomRatePlans;
}
