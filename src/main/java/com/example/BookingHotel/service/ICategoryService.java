package com.example.BookingHotel.service;


import com.example.BookingHotel.response.CategoryDto;
import com.example.BookingHotel.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    List<CategoryDto> getLstCategories();
}
