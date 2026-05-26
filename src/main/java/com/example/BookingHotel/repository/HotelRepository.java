package com.example.BookingHotel.repository;

import com.example.BookingHotel.model.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;


public interface HotelRepository extends JpaRepository<Hotel, Long> {
    //get hotel using hotelId (lấy khách sạn rồi liệt kê các phòng ma nó có)
    Optional<Hotel> findByHotelId(Long hotelId);

    List<Hotel> findByRateHotel(float rateHotel);

}
