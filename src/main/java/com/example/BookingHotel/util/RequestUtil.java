package com.example.BookingHotel.util;

import jakarta.servlet.http.HttpServletRequest;

public class RequestUtil {
    public static String getIpAddress(HttpServletRequest request) {
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        if (xForwardedForHeader == null) {
            String remoteAddress = request.getRemoteAddr();
            if (remoteAddress == null) {
                remoteAddress = "127.0.0.1";
            }
            return remoteAddress;
        }
        return xForwardedForHeader.split(",")[0].trim();
    }
}
