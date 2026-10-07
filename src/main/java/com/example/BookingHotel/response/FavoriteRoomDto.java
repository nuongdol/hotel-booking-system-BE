package com.example.BookingHotel.response;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteRoomDto {

    private Long favoriteRoomId;

    private Long userId;

    private Long roomId;
}
