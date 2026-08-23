package com.example.BookingHotel.repository;

import com.example.BookingHotel.dto.CityDto;
import com.example.BookingHotel.model.City;
import com.example.BookingHotel.response.CityResponse;
import com.example.BookingHotel.response.DetailCityResponse;
import com.example.BookingHotel.sql.SQLCity;
import com.example.BookingHotel.sql.SQLHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {

    @Query(nativeQuery = true, value = SQLCity.GET_CITY_IDS)
    List<Long> getCityIds();

    @Query(nativeQuery = true, value = SQLHotel.GET_COUNT_HOTEL_MIN_PRICES)
    List<CityDto> getListCity(@Param("cityIds") List<Long> cityIds);

    @Query(nativeQuery = true, value = SQLHotel.GET_CHEAPEST_ROOM_IN_CITY)
    List<DetailCityResponse> getDetailCheapestCity(Long cityId);
}
