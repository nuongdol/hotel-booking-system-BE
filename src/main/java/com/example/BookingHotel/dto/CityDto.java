package com.example.BookingHotel.dto;

import java.math.BigDecimal;

public interface CityDto {
     Long getCityId();
     String getName();
     Long getHotelsCount();
     BigDecimal getMinPrice();
     String getImageUrl();
}
