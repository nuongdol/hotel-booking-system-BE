package com.example.BookingHotel.repository;

import com.example.BookingHotel.model.Categories;
import com.example.BookingHotel.sql.SQLCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Long, Categories> {
    @Query(nativeQuery = true, value = SQLCategory.GET_LIST_CATEGORIES)
    List<Categories> getLstCategories();
}
