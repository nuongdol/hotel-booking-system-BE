package com.example.BookingHotel.service;


import com.example.BookingHotel.constant.ResponseCode;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.repository.VoucherRepository;
import com.example.BookingHotel.response.VoucherResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class VoucherServiceImp implements IVoucherService{

    private final VoucherRepository voucherRepository;
    @Override
    public Set<VoucherResponse> getVouchers() {
        Set<VoucherResponse> voucherResponses = voucherRepository.getVouchers();
        if(voucherResponses == null){
            throw new BusinessException(ResponseCode.LIST_VOUCHER_NOT_NULL);
        }
        if(voucherResponses.isEmpty()){
            throw new BusinessException(ResponseCode.LIST_VOUCHER_IS_EMPTY);
        }
        return voucherResponses;
    }
}
