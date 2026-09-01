package com.example.BookingHotel.repository;

import com.example.BookingHotel.model.Room;
import com.example.BookingHotel.response.DetailCityResponse;
import com.example.BookingHotel.sql.SQLRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@EnableJpaRepositories
@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();


    @Query("SELECT r FROM Room r " +
        "WHERE r.roomType LIKE %:roomType% " + // Thêm khoảng trắng sau %
        "AND r.id NOT IN (" +
        "SELECT br.room.id FROM BookedRoom br " +
        "WHERE (br.checkInDate <= :checkOutDate AND br.checkOutDate >= :checkInDate))") // Điều kiện thời gian đã được sửa
    List<Room> findAvailableRoomsByDatesAndType( LocalDate checkInDate,LocalDate checkOutDate,String roomType);;

    @Query(nativeQuery = true, value = SQLRoom.SEARCH_LIST_ROOM)
    List<DetailCityResponse> searchListRoom(@Param("city") String city,
                                            @Param("checkInDate") LocalDateTime checkInDate,
                                            @Param("checkOutDate") LocalDateTime checkOutDate,
                                            @Param("adults") Integer adults,
                                            @Param("children") Integer children,
                                            @Param("rooms") Integer rooms);
}
