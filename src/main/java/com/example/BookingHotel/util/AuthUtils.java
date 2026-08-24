package com.example.BookingHotel.util;

import com.example.BookingHotel.model.User;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthUtils {

    private AuthUtils() {

    }

    //lay thong tin nguoi dung hien tai
    public static User getCurrentUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext == null || securityContext.getAuthentication() == null
                || securityContext.getAuthentication().getPrincipal() == null) {
            return null;
        }
        Object principal = securityContext.getAuthentication().getPrincipal();
        if (principal instanceof String && principal.equals("anonymousUser")) {
            return null;
        }
        if (principal instanceof User) {
            return (User) principal;
        }
        return null;
    }
}
