package com.example.BookingHotel.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/voucher")
public class VoucherController {

    @PostMapping("/apply")
    @Operation(description = "áp dung voucher cho booking")

    @DeleteMapping("/remove")
    @Operation(description = "Huỷ mã giảm giá")

}
