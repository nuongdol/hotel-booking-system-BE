package com.example.BookingHotel.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MemberLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_level_id")
    Long MemberLevelId;

    @NotNull
    @Column(name = "level_name")
    String levelName;

    @Column(name = "discount_rate", precision = 5, scale = 2)
    BigDecimal discountRate = new BigDecimal("0.00");

    @Column(name = "min_total_spend", precision = 10, scale = 2)
    BigDecimal minTotalSpend = new BigDecimal("0.00");
}
