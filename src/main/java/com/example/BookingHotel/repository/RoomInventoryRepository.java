package com.example.BookingHotel.repository;

import com.example.BookingHotel.model.RoomInventory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface RoomInventoryRepository extends JpaRepository<RoomInventory, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE RoomInventory AS r SET r.stock = r.stock - 1,\n" +
            "r.version = r.version + 1 \n" +
            "WHERE r.room.id =: roomId and r.availableRoomDate =: date \n" +
            "and r.stock > 0")
    int decreaseStock(@Param("roomId") Long roomId, @Param("date") LocalDateTime date);
}
