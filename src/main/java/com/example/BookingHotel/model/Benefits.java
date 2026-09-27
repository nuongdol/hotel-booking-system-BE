package com.example.BookingHotel.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "benefits")
@NoArgsConstructor
@AllArgsConstructor
public class Benefits {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "benefit_id")
    private Long benefitId;

    @Column(name = "name_benefit")
    private String nameBenefit;

    @Column(name = "icon")
    private String icon;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "benefits_room_rate_plan",
            joinColumns = @JoinColumn(name = "benefit_id", referencedColumnName = "benefit_id"),
            inverseJoinColumns = @JoinColumn(name = "rate_plan_id", referencedColumnName = "rate_plan_id")
    )
    private Collection<RoomRatePlans> roomRatePlans = new ArrayList<>();
}
