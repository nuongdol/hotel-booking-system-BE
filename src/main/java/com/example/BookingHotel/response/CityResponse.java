package com.example.BookingHotel.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class CityResponse {
    private Long cityId;
    private String name;
    private Long hotelsCount;
    // phong re nhat trong các khách sạn thuộc thành pho du lich
    private BigDecimal minPrice;
    private String imageUrl;
    private String tagName;
}
