package com.example.BookingHotel.repository;

import com.example.BookingHotel.model.RoomInventory;
import com.example.BookingHotel.sql.SQLInventory;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface RoomInventoryRepository extends JpaRepository<RoomInventory, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE RoomInventory AS r SET r.stock = r.stock - 1,\n" +
            "r.version = r.version + 1 \n" +
            "WHERE r.room.id = :roomId and r.availableRoomDate = :date \n" +
            "and r.stock > 0")
    int decreaseStock(@Param("roomId") Long roomId, @Param("date") LocalDate date);

    @Query(nativeQuery = true, value = SQLInventory.AVAILABLE_ROOM_CHECK)
    RoomInventory findByAvailabilityRoom(@Param("roomId") Long roomId,
                                         @Param("checkinDate") LocalDate checkinDate,
                                         @Param("checkoutDate") LocalDate checkoutDate);
}
