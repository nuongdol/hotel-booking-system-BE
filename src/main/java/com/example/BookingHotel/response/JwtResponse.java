package com.example.BookingHotel.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtResponse {

    private Long id;

    private String email;

    private String accessToken;

    private String type = "Bearer";

    private List<String> roles;

    private UserResponse userResponse;

    private String statusOTP = "REQUIRE_OTP";

    private String preAuthenticationToken;

    private Boolean statusOtp;
}