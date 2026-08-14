package com.example.BookingHotel.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "seasonal_price")
@DynamicInsert
@DynamicUpdate
public class SeasonalPrice {

    @Id
    @Column(name = "seasonal_price_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seasonalPriceId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "started_date")
    @NotNull
    private LocalDateTime startedDate;

    @Column(name = "ended_date")
    @NotNull
    private LocalDateTime endedDate;

    @Column(name = "price_per_night", precision = 10, scale = 2)
    @NotNull
    BigDecimal pricePerNight;
}
