package com.example.BookingHotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

//dịch vu bo sung -tuy chon
@Entity
@Table(name = "add_on_services")
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToMany(mappedBy = "addOnService")
    private List<BookedRoom> bookedRoom = new ArrayList<>();
}
