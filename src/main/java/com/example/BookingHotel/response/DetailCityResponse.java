package com.example.BookingHotel.response;


import java.math.BigDecimal;

public interface DetailCityResponse {

    Long getCityId();
    String getCityName();
    Long getHotelId();
    String getNameHotel();
    String getAddressHotel();
    String getImageHotel();
    Float getRateHotel();
    String getLocation();
    String getWard();
    String getDistrict();
    String getLatitude();
    String getLongitude();
    String getBadgeLabel();
    String getRoomType();
    BigDecimal getOriginalPrice();
    BigDecimal getPrice();
    String getIsTaxIncluded();
    String getPolicyType();
    String getPolicyDescription();
    String getPromoLabel();
    String getDiscountValue();
}
