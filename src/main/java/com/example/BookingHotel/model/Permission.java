package com.example.BookingHotel.model;

import jakarta.persistence.*;

import java.util.Collection;
import java.util.HashSet;

@Entity
@Table(name = "permission")
public class Permission {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "permission_id")
    private Long permissionId;

    @Column(name = "permission_name")
    private String permissionName;

    @Column(name = "permission_key")
    private String permissionKey;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "role_permission",
    joinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "permission_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
    Collection<Role> roles = new HashSet<>();

}
