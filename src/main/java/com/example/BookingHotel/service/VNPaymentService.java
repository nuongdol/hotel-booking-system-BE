package com.example.BookingHotel.service;

import com.example.BookingHotel.request.InitPaymentRequest;
import com.example.BookingHotel.response.InitPaymentResponse;
import com.example.BookingHotel.util.DateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

@RequiredArgsConstructor
@Service
public class VNPaymentService implements IPaymentService{

    public static final String VERSION = "2.1.0";
    public static final String COMMAND = "pay";
    public static final String ORDER_TYPE = "190000";
    public static final long DEFAULT_MULTIPLIER = 100L;

    @Value("${payment.vnpay.tmn-code}")
    private String tmnCode;

    @Value("${payment.vnpay.init-payment-url}")
    private String initPaymentPrefixUrl;

    @Value("${payment.vnpay.return-url}")
    private String returnUrlFormat;

    @Value("${payment.vnpay.timeout}")
    private Integer paymentTimeout;

    private final CryptoService cryptoService;

    @Override
    public InitPaymentResponse init(InitPaymentRequest initPaymentRequest) {
        var amount = initPaymentRequest.getAmount() * DEFAULT_MULTIPLIER; // 1. amount *100;
        var txnRef = initPaymentRequest.getTxnRef(); //2. bookingId
        var returnUrl = buildReturnUrl(txnRef); // 3. FE redirect by returnUrl
        var vnCalendar = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        var createdDate = DateUtil.formatVnTime(vnCalendar);
        vnCalendar.add(Calendar.MINUTE, paymentTimeout);
        var expiredDate = DateUtil.formatVnTime(vnCalendar);//4. expiredDate for secure
        var ipAddress = initPaymentRequest.getIpAddress();
        var orderInfo = buildPaymentDetail(initPaymentRequest);
        Map<String, String> params = new HashMap<>();
        params.put(VNPay)
        return null;
    }

    private String buildPaymentDetail(InitPaymentRequest initPaymentRequest) {
        return String.format("Thanh toan don dat phong %s", initPaymentRequest.getTxnRef());
    }

    private String buildReturnUrl(String txnRef) {
        return String.format(returnUrlFormat, txnRef);
    }


}
