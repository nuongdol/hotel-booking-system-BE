package com.example.BookingHotel.service;

import com.example.BookingHotel.model.Hotel;
import com.example.BookingHotel.request.HotelRequest;
import com.example.BookingHotel.response.HotelResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface IHotelService {

    Hotel addHotel(HotelRequest hotelRequest) throws SQLException, IOException;

    Set<HotelResponse> getAllHotel();

    byte[] getImageHotelByHotelId(Long HotelId) throws SQLException;

    void deleteHotel(Long HotelId);

    HotelResponse updateHotel(Long HotelId, String nameHotel, String addressHotel);

    Optional<Hotel> getHotelById(Long hotelId);

    List<Hotel> getHotelByRate(float rateHotel);

    HotelResponse getDetailHotel(Long hotelId);

    HotelResponse updateImage(Long hotelId, MultipartFile imageHotel) throws IOException, SQLException;
}
