package com.example.BookingHotel.model;

import jakarta.persistence.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "booking_id")
    private BookedRoom bookedRoom;
}
