package com.example.BookingHotel.controller;

import com.example.BookingHotel.response.IpnResponse;
import com.example.BookingHotel.service.IpnHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final IpnHandler ipnHandler;
    //trả về trạng thái thanh toán
    @GetMapping("/vnpay_ipn")
    IpnResponse processIpn(@RequestParam Map<String, String> params){
        log.info("[VNPay Ipn] Params: {}", params);
        return ipnHandler.process(params);
    }
}
