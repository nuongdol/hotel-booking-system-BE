package com.example.BookingHotel.Enum;

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
