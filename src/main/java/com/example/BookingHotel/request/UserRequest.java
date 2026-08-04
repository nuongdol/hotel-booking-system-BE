package com.example.BookingHotel.request;

import com.example.BookingHotel.model.Role;
import jakarta.validation.constraints.NotNull;
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
public class UserRequest {

    @NotNull(message = "first-name cannot be null")
    private String firstName;

    @NotNull(message = "last-name cannot be null")
    private String lastName;

    @NotNull(message = "email cannot be null")
    private String email;

    @NotNull(message = "password cannot be null")
    private String password;

    private String phone;

    private String address;

    private Collection<Role> roles = new HashSet<>();
}
