package com.example.BookingHotel.response;

import com.example.BookingHotel.model.Promotions;

import java.math.BigDecimal;
import java.time.LocalDateTime;


public interface VoucherResponse {

    Long getVoucherId();

    String getVoucherCode();

    BigDecimal getDiscountAmount();

    String getDescription();

    Promotions getPromotions();

    Integer getUsedVoucherCount();

    Integer getUsageLimit();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    Long getBadgeId();

    String getCode();

    String getLabel();

    LocalDateTime getEndedAt();
}
