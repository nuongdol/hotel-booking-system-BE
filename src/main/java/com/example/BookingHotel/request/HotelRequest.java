package com.example.BookingHotel.request;

import com.example.BookingHotel.model.City;
import com.example.BookingHotel.model.Room;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class HotelRequest {

    @NotBlank(message = "Name hotel is not null")
    private String nameHotel;

    @NotBlank(message = "address hotel is not blank")
    private String addressHotel;

    private MultipartFile imageHotel;

    private Float rateHotel;

    private Set<Room> rooms;

    @NotBlank(message = "city is not blank")
    private String city;
}