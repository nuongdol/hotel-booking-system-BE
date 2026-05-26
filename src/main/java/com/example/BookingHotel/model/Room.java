package com.example.BookingHotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
@Entity
@Setter
@Getter
@AllArgsConstructor
public class Room {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "room_type")
    private String roomType;

    @Column(name = "room_price")
    private BigDecimal roomPrice;

    @Column(name = "is_booked")
    private boolean isBooked = false;

    @Lob
    @Column(name = "photo")
    private Blob photo;

    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<BookedRoom> bookings;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Hotel hotel;

    @Column(name = "max_children")
    private Integer maxChildren;

    @Column(name = "max_adults")
    private Integer maxAdults;

    public Room() {
        this.bookings = new ArrayList<>();
        //khoi tao danh sach dat phong
    }
    public void addBooking(BookedRoom booking){
        if(bookings == null){
            bookings = new ArrayList<>();
        }
        bookings.add(booking);//arrayList có hàm add
        booking.setRoom(this);//this o day la ban than classroom
        isBooked = true;
        String bookingCode = RandomStringUtils.randomNumeric(10);//bookingCode --> guests booked room
        booking.setBookingConfirmationCode(bookingCode);//khoi tao lai gia tri bookingCode sau khi nhan gia tri moi
    }
}
