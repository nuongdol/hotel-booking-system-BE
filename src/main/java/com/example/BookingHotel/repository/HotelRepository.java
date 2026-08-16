package com.example.BookingHotel.repository;

import com.example.BookingHotel.model.Hotel;
import com.example.BookingHotel.request.HotelDto;
import com.example.BookingHotel.sql.SQLHotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;


@Repository
public interface HotelRepository extends JpaRepository<Hotel, Long> {

    Optional<Hotel> findByHotelId(Long hotelId);

    List<Hotel> findByRateHotel(float rateHotel);

    @Query(nativeQuery = true, value = SQLHotel.GET_LIST_HOTEL)
    Set<HotelDto> getListHotel();

    @Query(nativeQuery = true, value = SQLHotel.GET_DETAIL_HOTEL)
    List<HotelDto> getDetailHotel(@Param("hotelId") Long hotelId);
}
