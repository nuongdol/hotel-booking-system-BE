package com.example.BookingHotel.response;

import java.time.LocalDateTime;

public interface CategoryDto {

    Long getCategoryId();

    String getLabel();

    String getIcon();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    Long getBadgeId();

    String getCode();

    String getLabelBadge();

    String getIconUrl();
}
