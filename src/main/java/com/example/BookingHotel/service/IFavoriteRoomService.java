package com.example.BookingHotel.service;


import com.example.BookingHotel.response.FavoriteRoomDto;

import java.util.List;

public interface IFavoriteRoomService {
    FavoriteRoomDto addFavoriteRoom(Long roomId);

    List<FavoriteRoomDto> getLstFavoriteRoom();
}
