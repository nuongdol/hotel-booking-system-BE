package com.example.BookingHotel.response;

import com.example.BookingHotel.model.City;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;

import java.util.Arrays;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
public class HotelResponse {
    private Long hotelId;
    private String nameHotel;
    private String addressHotel;
    private String imageHotel;
    private float rateHotel;
    private String cityName;
    private Set<RoomResponse> rooms;
    private City city;

    public HotelResponse(Long hotelId, String nameHotel, String addressHotel, float rateHotel) {
        this.hotelId = hotelId;
        this.nameHotel = nameHotel;
        this.addressHotel = addressHotel;
        this.rateHotel = rateHotel;
    }


    public HotelResponse(Long hotelId, String nameHotel, String addressHotel, byte[] imageHotel, float rateHotel
            , String cityName) {
        this.hotelId = hotelId;
        this.nameHotel = nameHotel;
        this.addressHotel = addressHotel;
        this.rateHotel = rateHotel;
        this.cityName = cityName;
        this.imageHotel = (imageHotel != null) ? Arrays.toString(Base64.encodeBase64String(imageHotel).getBytes()) : null;
    }
}
