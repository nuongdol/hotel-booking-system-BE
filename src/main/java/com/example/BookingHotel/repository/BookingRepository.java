package com.example.BookingHotel.repository;

import com.example.BookingHotel.model.BookedRoom;
import com.example.BookingHotel.response.InformationBookingRoom;
import com.example.BookingHotel.sql.SQLBooking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookedRoom, Long> {
    List<BookedRoom> findByRoomId(Long roomId);

    Optional<BookedRoom>findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> findByGuestEmail(String email);

    BookedRoom findByRoomIdAndUserId(Long roomId, Long userId);

    Optional<BookedRoom> findByBookingId(Long bookingId);

    @Query(nativeQuery = true, value = SQLBooking.INFORMATION_BOOKING_ROOM)
    InformationBookingRoom getInformationBookingRoom(@Param("roomId") Long roomId, @Param("id")Long id,
                                                     @Param("city") String city,
                                                     @Param("checkInDate") LocalDateTime checkInDate,
                                                     @Param("checkOutDate")LocalDateTime checkOutDate,
                                                     @Param("adults") Integer adults, @Param("children")Integer children);
}
