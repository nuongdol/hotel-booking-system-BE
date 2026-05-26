package com.example.BookingHotel.response;

import com.example.BookingHotel.model.City;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;



import java.sql.Blob;
import java.util.List;
@Data
@NoArgsConstructor
public class HotelResponse {
    private Long hotelId;
    private String nameHotel;
    private String addressHotel;
    private String imageHotel;
    private float rateHotel;
    private City city;
    private List<RoomResponse> rooms;

    public HotelResponse(Long hotelId, String nameHotel, String addressHotel, float rateHotel) {
        this.hotelId = hotelId;
        this.nameHotel = nameHotel;
        this.addressHotel = addressHotel;
        this.rateHotel = rateHotel;
    }


    public HotelResponse(Long hotelId, String nameHotel, String addressHotel, byte[] imageHotel, float rateHotel) {
        this.hotelId = hotelId;
        this.nameHotel = nameHotel;
        this.addressHotel = addressHotel;
        this.rateHotel = rateHotel;
        this.imageHotel = (imageHotel != null)? Base64.encodeBase64String(imageHotel):null;
    }
}
