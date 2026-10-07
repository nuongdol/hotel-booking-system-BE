package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.ResponseCode;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.repository.CategoryRepository;
import com.example.BookingHotel.response.CategoryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Repository
@RequiredArgsConstructor
public class CategoryService implements ICategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryDto> getLstCategories() {
        List<CategoryDto> lstCategories = categoryRepository.getLstCategories();
        if(lstCategories.isEmpty()){
            throw new BusinessException(ResponseCode.CATEGORY_IS_EMPTY);
        }
        return lstCategories;
    }
}
