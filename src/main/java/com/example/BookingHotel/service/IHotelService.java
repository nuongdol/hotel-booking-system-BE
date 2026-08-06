package com.example.BookingHotel.service;

import com.example.BookingHotel.model.Hotel;
import com.example.BookingHotel.request.HotelRequest;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IHotelService {

    Hotel addHotel(HotelRequest hotelRequest) throws SQLException, IOException;

    List<Hotel> getAllHotel();

    byte[] getImageHotelByHotelId(Long HotelId) throws SQLException;

    void deleteHotel(Long HotelId);

    Hotel updateRoom(Long HotelId, String nameHotel, String addressHotel, byte[] imageHotel);

    Optional<Hotel> getHotelById(Long hotelId);

    List<Hotel> getHotelByRate(float rateHotel);

}
