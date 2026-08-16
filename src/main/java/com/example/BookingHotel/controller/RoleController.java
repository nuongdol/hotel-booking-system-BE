package com.example.BookingHotel.controller;

import com.example.BookingHotel.exception.RoleAlreadyExistException;
import com.example.BookingHotel.model.Role;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.request.RoleDto;
import com.example.BookingHotel.response.ApiResponse;
import com.example.BookingHotel.response.RoleResponse;
import com.example.BookingHotel.service.IRoleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/role")
@Slf4j
public class RoleController {
    private final IRoleService roleService;


    @GetMapping()
    public ResponseEntity<List<Role>> getAllRoles() {
        return new ResponseEntity<>(roleService.getRoles(), HttpStatus.FOUND);
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<RoleResponse>> createRole(@Valid @RequestBody RoleDto theRole) {
        try {
            RoleResponse roleSave = roleService.createRole(theRole);
            log.info("Admin {} add new role {} for the system", null, roleSave.getName());
            ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                    .data(roleSave)
                    .code(HttpStatus.CREATED.value())
                    .message("New role created successfully!")
                    .build();
            return ResponseEntity.ok(response);
        } catch (RoleAlreadyExistException e) {
            ApiResponse<RoleResponse> response = ApiResponse.<RoleResponse>builder()
                    .code(HttpStatus.CONFLICT.value())
                    .message(e.getMessage())
                    .build();
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    @DeleteMapping("/{roleId}")
    public void deleteRole(@PathVariable("roleId") Long roleId) {
        roleService.deleteRole(roleId);
    }

    @PostMapping("/{roleId}")
    public Role removeAllUsersFromRole(@PathVariable("roleId") Long roleId) {
        return roleService.removeAllUsersFromRole(roleId);
    }

    @PostMapping("/remove-user-from-role")
    public User removeUserFromRole(
            @RequestParam("userId") Long userId,
            @RequestParam("roleId") Long roleId) {
        return roleService.removeUserFromRole(userId, roleId);
    }

    @PostMapping("/assign-user-to-role")
    public User assignUserToRole(
            @RequestParam("userId") Long userId,
            @RequestParam("roleId") Long roleId) {
        return roleService.assignRoleToUser(userId, roleId);
    }
}
