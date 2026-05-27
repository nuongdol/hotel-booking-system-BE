package com.example.BookingHotel.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "room_inventory")
@Data
public class RoomInventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_inventory_id")
    private Long roomInventoryId;

    @Column(name = "available_room_date")
    private LocalDate availableRoomDate;

    @Column(name = "stock")
    private Integer stock;

    //số lượng phòng được đặt
    @Column(name = "booked_room")
    private Integer bookedRoom;

    //hệ số giá, tăng giá vào cuối tuan/le tết
    @Column(name = "price_multiplier")
    private BigDecimal priceMultiplier;

    @Version
    @Column(name = "version")
    private Long version;

    @UpdateTimestamp
    @Column(name = "last_updated")//dùng debug dữ liệu
    private LocalDateTime lastUpdated;

    @ManyToOne(fetch = FetchType.LAZY) //N dòng trong inventory thuộc về 1 dòng
    @JoinColumn(name = "room_id")
    private Room room;
}
