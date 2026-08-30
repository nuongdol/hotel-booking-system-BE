package com.example.BookingHotel.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public interface RoomInventoryResponse {

     Long getRoomInventoryId();

     LocalDate getAvailableRoomDate();

     Integer getStock();

     Integer getQuantityOfBookedRoom();

     BigDecimal getPriceMultiplier();

     Long getVersion();

     LocalDateTime getLastUpdated();

     Integer getBookedRoom();
}
