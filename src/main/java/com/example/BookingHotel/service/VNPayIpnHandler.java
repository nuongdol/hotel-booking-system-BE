package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.VNPayParams;
import com.example.BookingHotel.constant.VnpIpnResponseConst;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.response.IpnResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class VNPayIpnHandler implements IpnHandler{

    private final VNPaymentService vnPaymentService;
    private final BookingService bookingService;
    @Override
    public IpnResponse process(Map<String, String> params) {
        if(!vnPaymentService.verifyIpn(params)){
            return VnpIpnResponseConst.SIGNATURE_FAILED;
        }
        IpnResponse response;
        var txnRef = params.get(VNPayParams.TXN_REF);
        try{
            Long bookingId = Long.parseLong(txnRef);
            bookingService.markBooked(bookingId);
            response = VnpIpnResponseConst.SUCCESS;
        }catch (BusinessException exception){
            switch (exception.getResponseCode()){
                case BOOKING_NOT_FOUND -> response = VnpIpnResponseConst.ORDER_NOT_FOUND;
                default -> response = VnpIpnResponseConst.UNKNOWN_ERROR;
            }
        }catch (Exception e){
            response = VnpIpnResponseConst.UNKNOWN_ERROR;
        }
        log.info("[VNPay Ipn] txnRef: {}, reponse: {}",txnRef, response);
        return response;
    }
}
