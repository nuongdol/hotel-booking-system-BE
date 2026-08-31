package com.example.BookingHotel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name= "tag")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tag_id")
    private Long tagId;

    @Column(name = "name")
    private String name;

    @Column(name = "slug")
    //chuoi dinh danh voi url
    private String slug;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags")
    private Collection<City> cities = new HashSet<>();
}
