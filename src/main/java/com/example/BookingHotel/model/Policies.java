package com.example.BookingHotel.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "policies")
public class Policies {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "policy_id")
    private Long policyId;
    //FK
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_rate_plans_id")
    private RoomRatePlans roomRatePlans;
    /*
    cancellation, breakfast, payment..
     */
    @Column(name = "type")
    private Integer type;

    @Column(name = "description")
    private String description;

    @Column(name = "icon")
    private String icon;

}
