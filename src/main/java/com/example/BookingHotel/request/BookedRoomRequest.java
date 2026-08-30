package com.example.BookingHotel.request;

import com.example.BookingHotel.model.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookedRoomRequest {

    private Long bookingId;

    private LocalDate checkInDate;

    private LocalDate checkOutDate;

    private String guestFullName;

    private String guestEmail;

    private int NumOfAdults;

    private int NumOfChildren;

    private int totalNumOfGuest;

    private BigDecimal discountAmount = new BigDecimal("0.00");

    private BigDecimal finalPrice;

    private Integer status;
    /*
    pending = 1, confirmed = 2, cancelled = 3, completed = 4,
    no_show = 5
     */
    private LocalDateTime cancelledAt;

    private String cancelReason;

    private LocalDateTime createdAt;

    private Long customerId;

    private User user;

    private String bookingConfirmationCode;//ma dat phong

    private Room room;

    private Payment payment;

    private Review review;

    private LocalDateTime expiredAt;

    private String ipAddress;

    private RoomRatePlans roomRatePlans;

    private Set<AddOnServices> addOnServices;

    private Set<Vouchers> vouchers = new HashSet<>();

    public  int calculationTotalNumOfGuest(int numOfAdults, int numOfChildren){
        this.totalNumOfGuest = numOfAdults + numOfChildren;
        return totalNumOfGuest;
    }

}
