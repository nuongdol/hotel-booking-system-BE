package com.example.BookingHotel.repository;

import com.example.BookingHotel.model.FavoriteRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface FavoriteRoomRepository extends JpaRepository<FavoriteRoom, Long> {

    List<FavoriteRoom> findByUserId(Long id);
}
