package com.example.BookingHotel.request;

import com.example.BookingHotel.model.City;
import com.example.BookingHotel.model.Hotel;
import com.example.BookingHotel.model.Room;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.sql.Blob;
import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class HotelDto {

    private Long hotelId;

    private String nameHotel;

    private String addressHotel;

    private Blob imageHotel;

    private float rateHotel;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String cityName;

    private Hotel hotel;

    private Set<Room> rooms;

    private City city;

}
