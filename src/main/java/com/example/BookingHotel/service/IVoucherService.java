package com.example.BookingHotel.service;

import com.example.BookingHotel.response.VoucherResponse;

import java.util.Set;

public interface IVoucherService {
    Set<VoucherResponse> getVouchers();
}
