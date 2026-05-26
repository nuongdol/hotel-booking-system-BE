package com.example.BookingHotel.service;

import com.example.BookingHotel.exception.ResourceNotFoundException;
import com.example.BookingHotel.model.Hotel;
import com.example.BookingHotel.repository.HotelRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class HotelService implements IHotelService {
    private final HotelRepository hotelRepository;
    //add new a hotel
    @Override
    public Hotel addNewHotel(MultipartFile file, String nameHotel, String addressHotel, float rateHotel) throws SQLException, IOException {
        Hotel hotel = new Hotel();
        hotel.setAddressHotel(addressHotel);
        hotel.setNameHotel(nameHotel);
        hotel.setRateHotel(rateHotel);
        if(!file.isEmpty()){
            byte[] imageHotel = file.getBytes();
            Blob imageBlob = new SerialBlob(imageHotel);//SerialBlob convert byte to blob
            hotel.setImageHotel(imageBlob);
        }

        return hotelRepository.save(hotel);
    }

    //get all hotel
    @Override
    public List<Hotel> getAllHotel() {
        return hotelRepository.findAll();
    }

    //get imageHotel of the hotel which you wants
    @Override
    public byte[] getImageHotelByHotelId(Long HotelId) throws SQLException {
        //check hotel has exited and get imageHotel and transition
        Optional<Hotel> theHotel = hotelRepository.findByHotelId(HotelId);
        if(theHotel.isEmpty()){
            throw new ResourceNotFoundException("Sorry, Hotel not found");
        }
        Blob getImageHotel = theHotel.get().getImageHotel();
        if(getImageHotel != null){
            return getImageHotel.getBytes(1,(int)getImageHotel.length());
        }else {
            return null;

        }
    }
    // delete hotel
    @Override
    @Transactional
    public void deleteHotel(Long HotelId) {
        Optional<Hotel> theHotel = hotelRepository.findByHotelId(HotelId);
        if(theHotel.isPresent()){
            hotelRepository.deleteById(HotelId);
        }

    }
    //update Room
    @Override
    public Hotel updateRoom(Long HotelId, String nameHotel, String addressHotel, byte[] imageHotel) {
        Hotel hotel = hotelRepository.findByHotelId(HotelId)
                .orElseThrow(()->new ResourceNotFoundException("Hotel not found"));
        if(nameHotel != null){
            hotel.setNameHotel(nameHotel);
        }
        if(addressHotel != null){
            hotel.setAddressHotel(addressHotel);
        }
        if(imageHotel != null && imageHotel.length > 0){
            try{
                hotel.setImageHotel(new SerialBlob(imageHotel));
            } catch (SerialException e) {
                throw new RuntimeException(e);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        return hotelRepository.save(hotel);
    }

    //get hotel by hotelId which request by users
    @Override
    public Optional<Hotel> getHotelById(Long hotelId) {
        return Optional.of(hotelRepository.findByHotelId(hotelId).get());
    }
    //get hotels which have ratings. Ratings are given by user.
    @Override
    public List<Hotel> getHotelByRate(float rateHotel){
        return hotelRepository.findByRateHotel(rateHotel);
    }

}
