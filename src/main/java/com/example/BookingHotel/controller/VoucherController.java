package com.example.BookingHotel.controller;

import com.example.BookingHotel.response.ApiResponse;
import com.example.BookingHotel.response.VoucherResponse;
import com.example.BookingHotel.service.IVoucherService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/api/v1/voucher")
@RequiredArgsConstructor
public class VoucherController {

    private final IVoucherService voucherService;

//    @PostMapping("/apply")
//    @Operation(description = "áp dung voucher cho booking")
//
//    @DeleteMapping("/remove")
//    @Operation(description = "Huỷ mã giảm giá")

    @GetMapping()
    @Operation(description = "lấy tất cả các mã giảm giá cho khách sạn co badge quy định được giảm giá")
    public ResponseEntity<ApiResponse<Set<VoucherResponse>>> getVouchers(){
        Set<VoucherResponse> voucherResponses = voucherService.getVouchers();
        ApiResponse<Set<VoucherResponse>> apiResponse = ApiResponse.<Set<VoucherResponse>>builder()
                .data(voucherResponses)
                .message("Get vouchers successfully!")
                .code(HttpStatus.OK.value())
                .status("200")
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
