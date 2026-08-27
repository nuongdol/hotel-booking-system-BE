package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.ResponseCode;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.exception.InternalServerException;
import com.example.BookingHotel.exception.ResourceNotFoundException;
import com.example.BookingHotel.mapper.RoomMapper;
import com.example.BookingHotel.model.Room;
import com.example.BookingHotel.repository.RoomRepository;
import com.example.BookingHotel.request.RoomRequest;
import com.example.BookingHotel.response.DetailCityResponse;
import com.example.BookingHotel.response.RoomResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoomService implements IRoomService {

    private final RoomRepository roomRepository;
    private final RoomMapper roomMapper;

    //add new a room
    @Override
    public RoomResponse addNewRoom(RoomRequest roomRequest) {
        Room room = new Room();
        room.setRoomType(roomRequest.getRoomType());
        room.setRoomPrice(roomRequest.getRoomPrice());
        if(roomRequest.getImageRoom() == null || roomRequest.getImageRoom().isEmpty()){
            throw new BusinessException(ResponseCode.IMAGE_IS_EMPTY);
        }
        try{
            byte[] photoBytes = roomRequest.getImageRoom().getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);//SerialBlob convert byte to blob
            room.setPhoto(photoBlob);
        }catch (SQLException ex){
            log.error("Error creating image for room ID: {}",room.getId(), ex);
            throw new BusinessException(ResponseCode.DATABASE_ERROR);
        }catch (IOException ex){
            log.error("File processing error for room ID: {}",room.getId(), ex);
            throw new BusinessException(ResponseCode.FILE_PROCESSING_ERROR);
        }
        roomRepository.save(room);
        return roomMapper.toRoomResponse(room);
    }
    //lấy loại phòng khác nhau
    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();//ham nay dung de chi su khac biet
    }

    @Override
    public List<Room> getAllRoom() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long roomId) throws SQLException {
        //xem phong co ton tai hay khong dieu kien lay anh va chuyen doi
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if (theRoom.isEmpty()) {
            throw new ResourceNotFoundException("Sorry, Room not found.");
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if (photoBlob != null) {
            return photoBlob.getBytes(1, (int) photoBlob.length());
        } else {
            return null;
        }

    }

    @Override
    public void deleteRoom(Long roomId) {
        Optional<Room> theRoom = roomRepository.findById(roomId);
        if (theRoom.isPresent()) {
            roomRepository.deleteById(roomId);
        }
    }

    @Override
    public Room updateRoom(Long roomId, String roomType, BigDecimal roomPrice, byte[] photoBytes) {
        Room room = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        if (roomType != null) {
            room.setRoomType(roomType);
        }
        if (roomPrice != null) {
            room.setRoomPrice(roomPrice);
        }
        if (photoBytes != null && photoBytes.length > 0) {
            try {
                room.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException ex) {
                throw new InternalServerException("Error updating room.");
            }

        }
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long roomId) {
        return Optional.of(roomRepository.findById(roomId).get());
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate checkInDate, LocalDate checkOutDate, String roomType) {
        return roomRepository.findAvailableRoomsByDatesAndType(checkInDate, checkOutDate, roomType);
    }

    @Override
    public List<DetailCityResponse> searchListRoom(String city, LocalDateTime checkInDate, Integer totalNights, Integer adults, Integer children) {
        LocalDateTime checkOutDate = checkInDate.plusDays(totalNights);
        List<DetailCityResponse> responses = roomRepository.searchListRoom(city, checkInDate, checkOutDate, adults, children);
        if(responses.isEmpty()){
            throw new BusinessException(ResponseCode.LIST_ROOM_IS_EMPTY);
        }
        if(responses == null){
            throw new BusinessException(ResponseCode.LIST_ROOM_IS_NULL);
        }
        return responses;
    }
}
