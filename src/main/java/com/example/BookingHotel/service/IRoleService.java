package com.example.BookingHotel.service;

import com.example.BookingHotel.model.Role;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.request.RoleDto;

import java.util.List;
import java.util.Optional;


public interface IRoleService {
    List<Role> getRoles();
    Role createRole(RoleDto theRole);

    void deleteRole(Long id);
    Role findByName(String name);

    User removeUserFromRole(Long userId, Long roleId);
    User assignRoleToUser(Long userId, Long roleId);
    Role removeAllUsersFromRole(Long roleId);
}
