package com.example.BookingHotel.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoomRequest {

    @NotNull(message = "Room type không được để trống")
    private String roomType;

    @NotNull(message = "Room price không được để trống")
    @DecimalMin(value ="0.0", inclusive = false, message = "Room price phải lớn hớn 0")
    private BigDecimal roomPrice;

    private MultipartFile imageRoom;

    private boolean isBooked;
}
