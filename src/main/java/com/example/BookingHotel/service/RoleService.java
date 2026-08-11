package com.example.BookingHotel.service;

import com.example.BookingHotel.constant.ResponseCode;
import com.example.BookingHotel.exception.BusinessException;
import com.example.BookingHotel.exception.UserAlreadyExistsException;
import com.example.BookingHotel.mapper.RoleMapper;
import com.example.BookingHotel.model.Role;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.repository.RoleRepository;
import com.example.BookingHotel.repository.UserRepository;
import com.example.BookingHotel.request.RoleDto;
import com.example.BookingHotel.response.RoleResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final RoleMapper roleMapper;

    @Override
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }


    @Override
    public RoleResponse createRole(RoleDto theRole) {
        String roleName = "ROLE_"+ theRole.getName().toUpperCase();//ROLE_roles-user
        Role role = new Role(roleName);
        if (roleRepository.existsByName(roleName)){
            throw new BusinessException(ResponseCode.ROLE_NOT_EXSIT);
        }
        roleRepository.save(role);
        return roleMapper.toRoleResponse(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        this.removeAllUsersFromRole(roleId);
        roleRepository.deleteById(roleId);
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByName(name).get();
    }

    @Override
    public User removeUserFromRole(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role> role = roleRepository.findById(roleId);
        if (role.isPresent() && role.get().getUsers().contains(user.get())){
            role.get().removeUserFromRole(user.get());
            roleRepository.save(role.get());
            return user.get();
        }
        throw new UsernameNotFoundException("User not found");
    }

    @Override
    public User assignRoleToUser(Long userId, Long roleId) {
        Optional<User> user = userRepository.findById(userId);
        Optional<Role>  role = roleRepository.findById(roleId);
        if (user.isPresent() && user.get().getRoles().contains(role.get())){
            throw new UserAlreadyExistsException(
                    user.get().getFirstName()+ " is already assigned to the" + role.get().getName()+ " role");
        }
        if (role.isPresent()){
            role.get().assignRoleToUser(user.get());
            roleRepository.save(role.get());
        }
        return user.get();
    }

    @Override
    public Role removeAllUsersFromRole(Long roleId) {
        Optional<Role> role = roleRepository.findById(roleId);
        role.ifPresent(Role::removeAllUsersFromRole);
        return roleRepository.save(role.get());
    }
}