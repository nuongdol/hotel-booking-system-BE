package com.example.BookingHotel.service;

import com.example.BookingHotel.model.Hotel;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface IHotelService {
    //add new Hotel
    Hotel addNewHotel(MultipartFile image, String nameHotel, String addressHotel, float rateHotel) throws SQLException, IOException;
    //get all hotel in the city
    List<Hotel> getAllHotel();
    //get image of hotel
    byte[] getImageHotelByHotelId(Long HotelId) throws SQLException;
    //delete room
    void deleteHotel(Long HotelId);
    //update hotel
    Hotel updateRoom(Long HotelId, String nameHotel, String addressHotel, byte[] imageHotel);
    //get hotel using hotelId
    Optional<Hotel> getHotelById(Long hotelId);
    //get hotels which have ratings. Ratings are given
    //by user
    List<Hotel> getHotelByRate(float rateHotel);

}
