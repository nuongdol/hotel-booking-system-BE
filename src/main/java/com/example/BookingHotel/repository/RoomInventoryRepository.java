package com.example.BookingHotel.repository;

import com.example.BookingHotel.model.RoomInventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomInventoryRepository extends JpaRepository<Long, RoomInventory> {
}
