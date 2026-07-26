package com.example.BookingHotel.constant;

public enum PaymentType {
    VN_PAY("VNpay");

    private final String value;

    PaymentType(String value){
        this.value = value;
    }
    public String getValue(){
        return value;
    }
}
