package com.example.BookingHotel.repository;

import com.example.BookingHotel.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


import java.time.LocalDate;
import java.util.List;
@EnableJpaRepositories
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();


    @Query("SELECT r FROM Room r " +
        "WHERE r.roomType LIKE %:roomType% " + // Thêm khoảng trắng sau %
        "AND r.id NOT IN (" +
        "SELECT br.room.id FROM BookedRoom br " +
        "WHERE (br.checkInDate <= :checkOutDate AND br.checkOutDate >= :checkInDate))") // Điều kiện thời gian đã được sửa
    List<Room> findAvailableRoomsByDatesAndType( LocalDate checkInDate,LocalDate checkOutDate,String roomType);



}
