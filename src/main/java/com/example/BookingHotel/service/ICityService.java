package com.example.BookingHotel.service;

import com.example.BookingHotel.response.CityResponse;
import com.example.BookingHotel.response.DetailCityResponse;

import java.util.List;

public interface ICityService {

    List<CityResponse> getCity();

    List<DetailCityResponse> getDetailCheapestCity(Long cityId);

    List<DetailCityResponse> getDetailPopularCity();
}
