package com.example.BookingHotel.model;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;

@Entity
@NoArgsConstructor
@Table(name = "badges")
public class Badges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "badge_id")
    private Long badgeId;

    @Column(name = "code")
    private String code;

    @Column(name = "label")
    private String label;

    @Column(name = "icon_url")
    private String iconUrl;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "badges_hotel",
            joinColumns = @JoinColumn(name = "badge_id", referencedColumnName = "badge_id"),
            inverseJoinColumns = @JoinColumn(name = "hotel_id", referencedColumnName = "hotel_id"))
    Collection<Hotel> hotels = new HashSet<>();
}
