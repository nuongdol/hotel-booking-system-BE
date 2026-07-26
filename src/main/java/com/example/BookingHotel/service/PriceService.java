package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.ResponseCode;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.model.Room;
import com.example.BookingHotel.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class PriceService implements IPriceService{
    private final RoomRepository roomRepository;

    @Override
    public BigDecimal calculate(Long roomId, LocalDate checkinDate, LocalDate checkoutDate) {
        BigDecimal totalPrice = BigDecimal.ZERO;
        Long days = ChronoUnit.DAYS.between(checkinDate, checkoutDate);
        Room room = roomRepository.findById(roomId).orElseThrow(
                ()-> new BusinessException(ResponseCode.ROOM_NOT_FOUND)
        );
        totalPrice = room.getRoomPrice().multiply(BigDecimal.valueOf(days));
        /*
        discount
         */
        return totalPrice;
    }
}
