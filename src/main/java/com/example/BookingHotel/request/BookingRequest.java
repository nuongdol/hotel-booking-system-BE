package com.example.BookingHotel.request;


import com.fasterxml.jackson.databind.PropertyNamingStrategy.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@JsonNaming(value = SnakeCaseStrategy.class)
public class BookingRequest {

    @NotNull(message = "customer_id cannot be null")
    private Long customerId;

    @NotNull(message = "checkin_date cannot be null")
    private LocalDate checkInDate;

    @NotNull(message = "checkout_date cannot be null")
    private LocalDate checkOutDate;

    @NotNull(message = "guest_full_name cannot be null")
    private String guestFullName;

    @NotBlank(message = "guest_email cannot be blank")
    private String guestEmail;

    private int NumOfAdults;

    private int NumOfChildren;

    private int totalNumOfGuest;

    private BigDecimal finalPrice;

    private String ipAddress;
}
