package com.example.BookingHotel.response;

import com.example.BookingHotel.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashSet;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponse {

    private String firstName;

    private String lastName;

    private String email;

    private String phone;

    private String address;

    private Collection<Role> roles = new HashSet<>();
}
