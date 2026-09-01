package com.example.BookingHotel.controller;

import com.example.BookingHotel.exception.PhotoRetrievalException;
import com.example.BookingHotel.exception.ResourceNotFoundException;
import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.model.Room;
import com.example.BookingHotel.request.RoomRequest;
import com.example.BookingHotel.response.ApiResponse;
import com.example.BookingHotel.response.DetailCityResponse;
import com.example.BookingHotel.response.RoomResponse;
import com.example.BookingHotel.service.BookingService;
import com.example.BookingHotel.service.IRoomService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/rooms")
public class RoomController {
    private final IRoomService roomService;
    private final BookingService bookingService;

    @PostMapping()
    @Operation(description = "created a new room")
//    @PreAuthorize("hasRole('ROLE_ADMIN','ROLE_OWNER')")
    public ResponseEntity<ApiResponse<RoomResponse>> addNewRoom(@Valid @ModelAttribute RoomRequest roomRequest) {
        RoomResponse saveRoom = roomService.addNewRoom(roomRequest);
        log.info("add new room: {}", saveRoom);
        ApiResponse<RoomResponse> response = ApiResponse.<RoomResponse>builder()
                .message("Add new room")
                .status("SUCCESS")
                .code(200)
                .data(saveRoom)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/type")
    public List<String> getRoomType() {
        return roomService.getAllRoomTypes();
    }


    @GetMapping()
    public ResponseEntity<List<RoomResponse>> getAllRooms() throws SQLException {
        List<Room> rooms = roomService.getAllRoom();
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : rooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String base64Photo = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(base64Photo);
                roomResponses.add(roomResponse);

            }

        }
        return ResponseEntity.ok(roomResponses);
    }

    //xoá phòng
    @DeleteMapping("/delete/room/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteRoom(@PathVariable("roomId") Long roomId) {
        roomService.deleteRoom(roomId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //cập nhật phòng
    @PutMapping("/{roomId}")
    @PreAuthorize("hasRole('ROLE_ADMIN', 'ROLE_OWNER')")
    public ResponseEntity<RoomResponse> updateRoom(@PathVariable Long roomId,
                                                   @RequestParam(required = false) String roomType,
                                                   @RequestParam(required = false) BigDecimal roomPrice,
                                                   @RequestParam(required = false) MultipartFile photo) throws IOException, SQLException {
        byte[] photoBytes = photo != null && !photo.isEmpty() ?
                photo.getBytes() : roomService.getRoomPhotoByRoomId(roomId);
        Blob photoBlob = photoBytes != null && photoBytes.length > 0 ? new SerialBlob(photoBytes) : null;
        Room theRoom = roomService.updateRoom(roomId, roomType, roomPrice, photoBytes);
        theRoom.setPhoto(photoBlob);
        RoomResponse roomResponse = getRoomResponse(theRoom);
        return ResponseEntity.ok(roomResponse);
    }

    //lấy id mã phòng
    @GetMapping("/room/{roomId}")
    public ResponseEntity<Optional<RoomResponse>> getRoomById(@PathVariable Long roomId) {
        Optional<Room> theRoom = roomService.getRoomById(roomId);
        return theRoom.map(room -> {
            RoomResponse roomResponse = getRoomResponse(room);
            return ResponseEntity.ok(Optional.of(roomResponse));
        }).orElseThrow(() -> new ResourceNotFoundException("Room not found."));
    }

    //Lấy phòng phù hợp
    @GetMapping("/available-rooms")
    public ResponseEntity<List<RoomResponse>> getAvailableRooms(@RequestParam("checkInDate") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate checkInDate,
                                                                @RequestParam("checkOutDate") @DateTimeFormat(pattern = "yyyy.MM.dd") LocalDate checkOutDate,
                                                                @RequestParam("roomType") String roomType) throws SQLException {
        List<Room> availableRooms = roomService.getAvailableRooms(checkInDate, checkOutDate, roomType);
        List<RoomResponse> roomResponses = new ArrayList<>();
        for (Room room : availableRooms) {
            byte[] photoBytes = roomService.getRoomPhotoByRoomId(room.getId());
            if (photoBytes != null && photoBytes.length > 0) {
                String photoBase64 = Base64.encodeBase64String(photoBytes);
                RoomResponse roomResponse = getRoomResponse(room);
                roomResponse.setPhoto(photoBase64);
                roomResponses.add(roomResponse);
            }
        }
        if (roomResponses.isEmpty()) {
            return ResponseEntity.noContent().build();

        } else {
            return ResponseEntity.ok(roomResponses);
        }
    }


    private RoomResponse getRoomResponse(Room room) {
        List<BookedRoom> bookings = getAllBookingsByRoomId(room.getId());
        byte[] photoBytes = null;
        Blob photoBlob = room.getPhoto();
        if (photoBlob != null) {
            try {
                photoBytes = photoBlob.getBytes(1, (int) photoBlob.length());
            } catch (SQLException e) {
                throw new PhotoRetrievalException("Error retrieving photo");

            }
        }
        return new RoomResponse(room.getId(), room.getRoomType(), room.getRoomPrice(), room.isBooked(), photoBytes);
    }

    private List<BookedRoom> getAllBookingsByRoomId(Long roomId) {
        return bookingService.getAllBookingsByRoomId(roomId);

    }

    //tìm kiếm phòng khach sạn cua user
    @GetMapping("/research")
    public ResponseEntity<ApiResponse<List<DetailCityResponse>>> searchListRoom(
            @RequestParam(value = "city", required = false, defaultValue = "Đà Nẵng") String city,
            @RequestParam(value = "checkInDate", required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate checkInDate,
            @RequestParam(value = "totalNights", required = false, defaultValue = "1") Integer totalNights,
            @RequestParam(value = "adults", required = false, defaultValue = "1") Integer adults,
            @RequestParam(value = "children", required = false, defaultValue = "0") Integer children,
            @RequestParam(value = "rooms", required = false, defaultValue = "1") Integer rooms) {

        //chuyển đổi LocalDate thành LocalDateTime
        LocalDateTime checkInDateTime = (checkInDate != null) ? checkInDate.atStartOfDay() : null;
        List<DetailCityResponse> lstRoom = roomService.searchListRoom(city, checkInDateTime, totalNights, adults, children, rooms);
        ApiResponse<List<DetailCityResponse>> apiResponse = ApiResponse.<List<DetailCityResponse>>builder()
                .status("200")
                .code(HttpStatus.OK.value())
                .message("Search List Room Successfully!")
                .data(lstRoom)
                .build();
        return ResponseEntity.ok(apiResponse);
    }
}
