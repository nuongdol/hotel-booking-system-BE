package com.example.BookingHotel.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class HoleRoom {
    private Long bookId;
    private Long roomId;
    private LocalDateTime expiredAt;
    private boolean canRebook;
    private String redirectUrl;
}
