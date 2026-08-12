package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.ResponseCode;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.exception.ResourceNotFoundException;
import com.example.BookingHotel.mapper.RoomMapper;
import com.example.BookingHotel.model.City;
import com.example.BookingHotel.model.Hotel;
import com.example.BookingHotel.repository.CityRepository;
import com.example.BookingHotel.repository.HotelRepository;
import com.example.BookingHotel.repository.RoomRepository;
import com.example.BookingHotel.request.HotelDto;
import com.example.BookingHotel.request.HotelRequest;
import com.example.BookingHotel.response.HotelResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class HotelService implements IHotelService {

    private final HotelRepository hotelRepository;
    private final CityRepository cityRepository;
    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    //add new a hotel
    @Override
    public Hotel addHotel(HotelRequest hotelRequest) throws SQLException, IOException {
        //save city
        City city = new City();
        city.setNameCity(hotelRequest.getNameHotel());
        City citySave = cityRepository.save(city);
        //save hotel
        Hotel hotel = new Hotel();
        hotel.setAddressHotel(hotel.getAddressHotel());
        hotel.setNameHotel(hotel.getNameHotel());
        hotel.setRateHotel(hotel.getRateHotel());
        if (!hotelRequest.getImageHotel().isEmpty()) {
            byte[] imageHotel = hotelRequest.getImageHotel().getBytes();
            Blob imageBlob = new SerialBlob(imageHotel);//SerialBlob convert byte to blob
            hotel.setImageHotel(imageBlob);
        }
        hotel.setCreatedAt(LocalDateTime.now());
        hotel.setCity(citySave);
        return hotelRepository.save(hotel);
    }

    //get all hotel
    @Override
    public Set<HotelResponse> getAllHotel() {
        Set<HotelDto> lstHotel = hotelRepository.getListHotel();
        if (lstHotel.isEmpty()) {
            throw new BusinessException(ResponseCode.LIST_HOTEL_IS_EMPTY);
        }
        return lstHotel.stream()
                .map(hotel -> {
                    Blob image = hotel.getImageHotel();
                    String base64Image = convertBlobToStringTypeOfImage(image);
                    return HotelResponse.builder()
                            .hotelId(hotel.getHotelId())
                            .addressHotel(hotel.getAddressHotel())
                            .imageHotel(base64Image)
                            .cityName(hotel.getCityName())
                            .nameHotel(hotel.getNameHotel())
                            .rateHotel(hotel.getRateHotel())
                            .build();
                }).collect(Collectors.toSet());
    }

    //get imageHotel of the hotel which you wants
    @Override
    public byte[] getImageHotelByHotelId(Long hotelId) throws SQLException {
        //check hotel has exited and get imageHotel and transition
        Optional<Hotel> theHotel = hotelRepository.findByHotelId(hotelId);
        if (theHotel.isEmpty()) {
            throw new ResourceNotFoundException("Sorry, Hotel not found");
        }
        Blob getImageHotel = theHotel.get().getImageHotel();
        if (getImageHotel != null) {
            return getImageHotel.getBytes(1, (int) getImageHotel.length());
        } else {
            return null;

        }
    }

    // delete hotel
    @Override
    @Transactional
    public void deleteHotel(Long HotelId) {
        Optional<Hotel> theHotel = hotelRepository.findByHotelId(HotelId);
        if (theHotel.isPresent()) {
            hotelRepository.deleteById(HotelId);
        }

    }

    @Override
    public HotelResponse updateHotel(Long HotelId, String nameHotel, String addressHotel) {
        Hotel hotel = hotelRepository.findByHotelId(HotelId)
                .orElseThrow(() -> new BusinessException(ResponseCode.HOTEL_NOT_FOUND));
        if (nameHotel != null) {
            hotel.setNameHotel(nameHotel);
        }
        if (addressHotel != null) {
            hotel.setAddressHotel(addressHotel);
        }
        hotelRepository.save(hotel);
        String image = convertBlobToStringTypeOfImage(hotel.getImageHotel());
        return HotelResponse.builder()
                .nameHotel(hotel.getNameHotel())
                .hotelId(hotel.getHotelId())
                .addressHotel(hotel.getAddressHotel())
                .imageHotel(image)
                .rateHotel(hotel.getRateHotel())
                .rooms(hotel.getRooms().stream()
                        .map(roomMapper::toRoomResponse)
                        .collect(Collectors.toSet()))
                .cityName(hotel.getCity() != null ? hotel.getCity().getNameCity() : null)
                .build();
    }

    @Override
    public Optional<Hotel> getHotelById(Long hotelId) {
        return Optional.empty();
    }

    //get hotels which have ratings. Ratings are given by user.
    @Override
    public List<Hotel> getHotelByRate(float rateHotel) {
        return hotelRepository.findByRateHotel(rateHotel);
    }

    @Override
    public HotelResponse getDetailHotel(Long hotelId) {
        //lay khach san
        Hotel hotel = hotelRepository.findByHotelId(hotelId)
                .orElseThrow(() -> new BusinessException(ResponseCode.HOTEL_NOT_FOUND));
        String image = convertBlobToStringTypeOfImage(hotel.getImageHotel());
        return HotelResponse.builder()
                .nameHotel(hotel.getNameHotel())
                .hotelId(hotel.getHotelId())
                .addressHotel(hotel.getAddressHotel())
                .imageHotel(image)
                .rateHotel(hotel.getRateHotel())
                .rooms(hotel.getRooms().stream()
                        .map(roomMapper::toRoomResponse)
                        .collect(Collectors.toSet()))
                .cityName(hotel.getCity().getNameCity())
                .build();
    }

    @Override
    public HotelResponse updateImage(Long hotelId, MultipartFile imageHotel) {
        //kiểm tra file ảnh đầu vào
        if (imageHotel == null || imageHotel.isEmpty()) {
            throw new BusinessException(ResponseCode.IMAGE_IS_EMPTY);
        }
        try {
            Hotel hotel = hotelRepository.findByHotelId(hotelId)
                    .orElseThrow(() -> new BusinessException(ResponseCode.HOTEL_NOT_FOUND));
            //chuyen multipartFile sang blob
            byte[] byteImage = imageHotel.getBytes();
            Blob blobImage = new SerialBlob(byteImage);
            hotel.setImageHotel(blobImage);
            Hotel updateHotel = hotelRepository.save(hotel);
            String imageUrl = convertBlobToStringTypeOfImage(updateHotel.getImageHotel());
            return HotelResponse.builder()
                    .hotelId(updateHotel.getHotelId())
                    .nameHotel(updateHotel.getNameHotel())
                    .addressHotel(updateHotel.getAddressHotel())
                    .rateHotel(updateHotel.getRateHotel())
                    .imageHotel(imageUrl)
                    .cityName(updateHotel.getCity() != null ? updateHotel.getCity().getNameCity() : null)
                    .rooms(updateHotel.getRooms().stream()
                            .map(roomMapper::toRoomResponse)
                            .collect(Collectors.toSet()))
                    .build();
        } catch (SQLException ex) {
            log.error("Error Updating image for hotel ID: {}", hotelId, ex);
            throw new BusinessException(ResponseCode.DATABASE_ERROR);
        } catch (IOException ex) {
            log.error("File processing error for hotel ID: {}", hotelId, ex);
            throw new BusinessException(ResponseCode.FILE_PROCESSING_ERROR);
        }
    }

    private String convertBlobToStringTypeOfImage(Blob imageHotel) {
        String base64Image = null;
        if (imageHotel != null) {
            try {
                byte[] byteImage = imageHotel.getBytes(1, (int) imageHotel.length());
                base64Image = Base64.encodeBase64String(byteImage);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return base64Image;
    }
}
