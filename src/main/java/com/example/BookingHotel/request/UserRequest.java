package com.example.BookingHotel.request;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


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
    @Email(message = "email must be valid")
    private String email;

    @NotNull(message = "password cannot be null")
    private String password;

    private String phone;

    private String address;
}
