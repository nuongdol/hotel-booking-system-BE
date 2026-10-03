package com.example.BookingHotel.service;


import com.example.BookingHotel.response.CategoryResponse;

import java.util.List;

public interface ICategoryService {
    List<CategoryResponse> getLstCategories();
}
