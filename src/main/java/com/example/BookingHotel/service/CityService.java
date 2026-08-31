package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.ResponseCode;
import com.example.BookingHotel.dto.CityDto;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.repository.CityRepository;
import com.example.BookingHotel.response.CityResponse;
import com.example.BookingHotel.response.DetailCityResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService implements ICityService{

    private final CityRepository cityRepository;

    @Override
    public List<CityResponse> getCity() {
        //lay danh sách thanh pho co trong bang city
        List<Long> cityIds = cityRepository.getCityIds();
        if(cityIds.isEmpty()){
            throw new BusinessException(ResponseCode.CITY_NOT_FOUND);
        }
        //lay giá phong thap nhat va so khach san trong thanh pho
        List<CityDto> lstCityDto = cityRepository.getListCity(cityIds);
        List<CityResponse> lstCityResponse = lstCityDto.stream().map(city ->{
            CityResponse response = new CityResponse();
            response.setCityId(city.getCityId());
            response.setName(city.getName());
            response.setHotelsCount(city.getHotelsCount());
            response.setImageUrl(city.getImageUrl());
            response.setMinPrice(city.getMinPrice());
            response.setTagName(city.getTagName());
            return response;
        }).toList();
        if(lstCityResponse == null || lstCityResponse.isEmpty()){
            throw new BusinessException(ResponseCode.CITY_HAVE_NOT_HOTEL);
        }
        return lstCityResponse;
    }

    @Override
    public List<DetailCityResponse> getDetailCheapestCity(Long cityId) {
        List<DetailCityResponse> responses = cityRepository.getDetailCheapestCity(cityId);
        if(responses == null || responses.isEmpty()){
            throw new BusinessException(ResponseCode.CHEAPEST_ROOM_NOT_FOUND);
        }
        return responses;
    }

    @Override
    public List<DetailCityResponse> getDetailPopularCity() {
        List<DetailCityResponse> responses = cityRepository.getDetailPopularCity();
        if(responses == null || responses.isEmpty()){
            throw new BusinessException(ResponseCode.POPULAR_ROOM_NOT_FOUND);
        }
        return responses;
    }
}
