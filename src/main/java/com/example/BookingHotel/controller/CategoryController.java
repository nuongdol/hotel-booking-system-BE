package com.example.BookingHotel.controller;


import com.example.BookingHotel.response.ApiResponse;
import com.example.BookingHotel.response.CategoryDto;
import com.example.BookingHotel.response.CategoryResponse;
import com.example.BookingHotel.service.ICategoryService;
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
@RequiredArgsConstructor
@RequestMapping("/api/v1/category")
@Slf4j
public class CategoryController {

    private final ICategoryService categoryService;

    @GetMapping("")
    @Operation(description = "Hiển thị danh sách categories của hotel-booking")
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getLstCategories(){
        log.info("-- Execute getLstCategories method: Start--");
        List<CategoryDto> categoryResponse = categoryService.getLstCategories();
        log.info("-- Execute getLstCategories method: End--");
        ApiResponse<List<CategoryDto>> response = ApiResponse.<List<CategoryDto>> builder()
                .status("SUCCESS")
                .data(categoryResponse)
                .code(HttpStatus.CREATED.value())
                .message("Successful get list categories")
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
