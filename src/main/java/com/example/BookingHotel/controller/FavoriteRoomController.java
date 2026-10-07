package com.example.BookingHotel.controller;


import com.example.BookingHotel.response.ApiResponse;
import com.example.BookingHotel.response.FavoriteRoomDto;
import com.example.BookingHotel.service.IFavoriteRoomService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/favorite-room")
@Slf4j
public class FavoriteRoomController {

    private final IFavoriteRoomService favoriteRoomService;

    @PostMapping("/{roomId}")
    public ResponseEntity<ApiResponse<FavoriteRoomDto>> addFavoriteRoom(
            @PathVariable Long roomId
    ){
        log.info("Start addFavoriteRoom at room {}: execute!", roomId);
        FavoriteRoomDto favoriteRoom = favoriteRoomService.addFavoriteRoom(roomId);
        log.info("End addFavoriteRoom at room {}: stop!", roomId);
        ApiResponse<FavoriteRoomDto> response = ApiResponse.<FavoriteRoomDto>builder()
                .message("Add favorite room successfully!")
                .data(favoriteRoom)
                .code(HttpStatus.OK.value())
                .status("SUCCESS")
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("")
    @Operation(description = "lấy tất cả các phòng ưu thích của user")
    public ResponseEntity<ApiResponse<List<FavoriteRoomDto>>> getLstFavoriteRoom(){
        log.info("Start getLstFavoriteRoom: execute!");
        List<FavoriteRoomDto> lstFavoriteRoom = favoriteRoomService.getLstFavoriteRoom();
        log.info("End getLstFavoriteRoom: stop!");
        ApiResponse<List<FavoriteRoomDto>> response = ApiResponse.<List<FavoriteRoomDto>> builder()
                .message("Get list favorite room successfully")
                .status("SUCCESS")
                .data(lstFavoriteRoom)
                .code(HttpStatus.OK.value())
                .build();
        return ResponseEntity.ok(response);
    }
}
