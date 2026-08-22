package com.example.BookingHotel.controller;

import com.example.BookingHotel.response.ApiResponse;
import com.example.BookingHotel.response.CityResponse;
import com.example.BookingHotel.service.ICityService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/city")
@RequiredArgsConstructor
public class CityController {

    private final ICityService cityService;

    @GetMapping(" ")
    @Operation(description = "lay thanh pho du lich")
    public ResponseEntity<ApiResponse<List<CityResponse>>> getCity() {
        List<CityResponse> lstCity = cityService.getCity();
        ApiResponse<List<CityResponse>> response = ApiResponse.<List<CityResponse>>builder()
                .message("Get list city successfully!")
                .data(lstCity)
                .code(HttpStatus.OK.value())
                .status("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }
}
