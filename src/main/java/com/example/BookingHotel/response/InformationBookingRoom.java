package com.example.BookingHotel.response;


import java.math.BigDecimal;

public interface InformationBookingRoom {
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
    Long getUserId();
    String getFirstName();
    String getLastName();
    String getPhone();
    String getAddress();
    Long getRoomId();
    BigDecimal getDiscountAmount();
    String getVoucherCode();
    Integer getUsedVoucherCount();
    Integer getUsageLimit();
}
