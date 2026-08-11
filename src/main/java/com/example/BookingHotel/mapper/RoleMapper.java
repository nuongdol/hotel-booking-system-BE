package com.example.BookingHotel.mapper;
import com.example.BookingHotel.model.Role;
import com.example.BookingHotel.response.RoleResponse;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface RoleMapper {

    RoleResponse toRoleResponse(Role role);
}
