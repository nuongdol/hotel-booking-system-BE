package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.ResponseCode;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.model.FavoriteRoom;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.repository.FavoriteRoomRepository;
import com.example.BookingHotel.repository.UserRepository;
import com.example.BookingHotel.response.FavoriteRoomDto;
import com.example.BookingHotel.util.AuthUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FavoriteRoomService implements IFavoriteRoomService{

    private final UserRepository userRepository;
    private final FavoriteRoomRepository favoriteRoomRepository;
    @Override
    public FavoriteRoomDto addFavoriteRoom(Long roomId) {
        User currentUser = AuthUtils.getCurrentUser();

        if(currentUser == null){
            throw new BusinessException(ResponseCode.USER_NOT_FOUND);
        }
        //kiểm tra user trong database
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(()-> new BusinessException(ResponseCode.USER_NOT_FOUND));
        FavoriteRoom favoriteRoom = new FavoriteRoom();
        favoriteRoom.setRoomId(roomId);
        favoriteRoom.setUserId(user.getId());
        favoriteRoomRepository.save(favoriteRoom);
        FavoriteRoomDto saveFavoriteRoom = new FavoriteRoomDto();
        BeanUtils.copyProperties(favoriteRoom,saveFavoriteRoom);
        return saveFavoriteRoom;
    }

    @Override
    public List<FavoriteRoomDto> getLstFavoriteRoom() {
        User currentUser = AuthUtils.getCurrentUser();

        if(currentUser == null){
            throw new BusinessException(ResponseCode.USER_NOT_FOUND);
        }
        //kiểm tra user trong database
        User user = userRepository.findByEmail(currentUser.getEmail())
                .orElseThrow(()-> new BusinessException(ResponseCode.USER_NOT_FOUND));
        List<FavoriteRoom> lstFavoriteRoom = favoriteRoomRepository.findByUserId(user.getId());
        if(lstFavoriteRoom == null || lstFavoriteRoom.isEmpty()){
            throw new BusinessException((ResponseCode.LIST_ROOM_IS_NULL));
        }
        List<FavoriteRoomDto> lstFavoriteRoomRepo =lstFavoriteRoom.stream().map(
                (room)->{
                    FavoriteRoomDto favoriteRoomDto = new FavoriteRoomDto();
                    BeanUtils.copyProperties(room, favoriteRoomDto);
                    return favoriteRoomDto;
                }).toList();
        return lstFavoriteRoomRepo;
    }
}
