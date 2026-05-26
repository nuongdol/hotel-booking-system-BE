package com.example.BookingHotel.controller;

import com.example.BookingHotel.exception.ResourceNotFoundException;
import com.example.BookingHotel.model.Hotel;
import com.example.BookingHotel.response.HotelResponse;
import com.example.BookingHotel.service.HotelService;
import com.example.BookingHotel.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hotels")
public class HotelController {
    private final HotelService hotelService;
    private final RoomService roomService;
    @PostMapping("/add/new-hotel")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<HotelResponse> addNewHotel(@RequestParam("image") MultipartFile imageHotel,
                                                     @RequestParam("name") String nameHotel,
                                                     @RequestParam("address") String addressHotel,
                                                     @RequestParam("rate") String rateHotel) throws SQLException, IOException {
        Hotel saveHotel = hotelService.addNewHotel(imageHotel,nameHotel,addressHotel, Float.parseFloat(rateHotel));
        //hotel display
//        HotelResponse response = getHotelResponse(saveHotel);
        HotelResponse response = new HotelResponse(saveHotel.getHotelId(),saveHotel.getNameHotel(), saveHotel.getAddressHotel(), saveHotel.getRateHotel());
        return ResponseEntity.ok(response);
    }
    @GetMapping("/all-hotels")
    //return list hotelResponse with getAllHotel
    public ResponseEntity<List<HotelResponse>> getAllHotel() throws SQLException{
        List<Hotel> hotels = hotelService.getAllHotel();
        List<HotelResponse> hotelResponses = new ArrayList<>();
        for(Hotel hotel: hotels){
            byte[] imageHotels = hotelService.getImageHotelByHotelId(hotel.getHotelId());
            if(imageHotels != null && imageHotels.length > 0){
                String base64Image = Base64.encodeBase64String(imageHotels);
                HotelResponse hotelResponse = getHotelResponse(hotel);
                hotelResponse.setImageHotel(base64Image);
                hotelResponses.add(hotelResponse);
            }
        }
        return ResponseEntity.ok(hotelResponses);
    }
    @GetMapping("/hotel/{hotelId}")
    public ResponseEntity<Optional<HotelResponse>>getHotelById(@PathVariable("hotelId") Long hotelId){
        Optional<Hotel> theHotel = hotelService.getHotelById(hotelId);
        return theHotel.map(hotel ->{
            HotelResponse hotelResponse = getHotelResponse(hotel);
            return ResponseEntity.ok(Optional.of(hotelResponse));
        }).orElseThrow(()->new ResourceNotFoundException("Hotel not found"));
    }
    @DeleteMapping("/delete/hotel/{hotelId}")
    public ResponseEntity<Void> deleteHotel(@PathVariable("hotelId") Long hotelId){
        hotelService.deleteHotel(hotelId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PutMapping("/update/{hotelId}")
    public ResponseEntity<HotelResponse> updateHotel(@PathVariable(name = "hotelId") Long hotelId,
                                                     @RequestParam(required = false, name = "name")  String nameHotel,
                                                     @RequestParam(required = false, name = "address") String addressHotel,
                                                     @RequestParam(required = false, name = "image") MultipartFile imageHotel) throws IOException, SQLException{
        byte[] imageHotels = imageHotel != null && !imageHotel.isEmpty() ?
                imageHotel.getBytes() : hotelService.getImageHotelByHotelId(hotelId);
        Blob imageBlob = imageHotels != null && imageHotels.length > 0 ? new SerialBlob(imageHotels) : null;
        Hotel theHotel = hotelService.updateRoom(hotelId, nameHotel, addressHotel, imageHotels);
        theHotel.setImageHotel(imageBlob);
        HotelResponse hotelResponse = getHotelResponse(theHotel);
        return ResponseEntity.ok(hotelResponse);
    }
    @GetMapping("/get-hotel-rating")
    public ResponseEntity<List<HotelResponse>> getHotelByRateHotel(@RequestParam("rate") float rateHotel){
        List<Hotel> theHotels = hotelService.getAllHotel();
        List <HotelResponse> hotels = new ArrayList<>();
        for(Hotel hotel : theHotels) {
            if (rateHotel == hotel.getRateHotel()) {
                HotelResponse hotelResponse = getHotelResponse(hotel);
                hotels.add(hotelResponse);
            }
        }

        return ResponseEntity.ok(hotels);
    }

    private HotelResponse getHotelResponse(Hotel hotel) {
        byte[] imageBytes = null;
        Blob imageBlob = hotel.getImageHotel();
        if(imageBlob != null){
            try{
                imageBytes = imageBlob.getBytes(1,(int)imageBlob.length());
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
       return new HotelResponse(hotel.getHotelId(), hotel.getNameHotel(), hotel.getAddressHotel(), imageBytes, hotel.getRateHotel());
        //convert hotel to hotelResponse
        /* HotelResponse response = new HotelResponse();
        response.setHotelId(hotel.getHotelId());
        response.setNameHotel(hotel.getAddressHotel());
        response.setRateHotel(hotel.getRateHotel());
        response.setImageHotel(Arrays.toString(imageBytes));
        return response;
        */
    }
}
