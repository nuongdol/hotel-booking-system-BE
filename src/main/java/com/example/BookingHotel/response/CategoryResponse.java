package com.example.BookingHotel.response;

import com.example.BookingHotel.model.Badges;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.HashSet;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryResponse {

    private Long categoryId;

    private String label;

    private String icon;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    Collection<Badges> badges = new HashSet<>();
}
