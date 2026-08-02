package com.example.BookingHotel.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponse {
    private Long id;
    private String email;
    private String accessToken;
    private String type = "Bearer";
    private List<String> roles;

    public JwtResponse(Long id, String email, String accessToken,
                       List<String> roles) {
        this.id = id;
        this.email = email;
        this.accessToken = accessToken;
        this.roles = roles;
    }
}