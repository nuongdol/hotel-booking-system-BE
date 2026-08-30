package com.example.BookingHotel.model;

import jakarta.persistence.*;
import lombok.Cleanup;

import java.math.BigDecimal;

//dịch vu bo sung -tuy chon
@Entity
@Table(name = "add_on_services")
public class AddOnServices {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "add_on_service_id")
    private Long addOnServiceId;

    @Column(name = "service_name")
    private String serviceName;

    @Column(name = "selected")
    private String selected;

    @Column(name = "description")
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "unit")
    private String unit;

    /*
    0: non active
    1: active
     */
    @Column(name = "is_active")
    private Integer isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private BookedRoom bookedRoom;
}
