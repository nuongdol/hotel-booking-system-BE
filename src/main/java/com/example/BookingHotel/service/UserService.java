package com.example.BookingHotel.service;

import com.example.BookingHotel.exception.UserAlreadyExistsException;
import com.example.BookingHotel.model.Role;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.repository.RoleRepository;
import com.example.BookingHotel.repository.UserRepository;
import com.example.BookingHotel.request.UserRequest;
import com.example.BookingHotel.response.UserResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public UserResponse registerUser(UserRequest user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        User userRegister = User.builder()
                .address(user.getAddress())
                .phone(user.getPhone())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .email(user.getEmail())
                .password(passwordEncoder.encode(user.getPassword()))
                .roles(Collections.singletonList(userRole))
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(userRegister);
        return UserResponse.builder()
                .address(user.getAddress())
                .phone(user.getPhone())
                .email(user.getEmail())
                .lastName(user.getLastName())
                .firstName(userRegister.getFirstName())
                .roles(Collections.singletonList(userRole))
                .build();
    }

    public UserResponse registerAdmin(UserRequest admin) {
        if (userRepository.existsByEmail(admin.getEmail())) {
            throw new UserAlreadyExistsException(admin.getEmail() + "already exists");
        }
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        User adminRegister = User.builder()
                .address(admin.getAddress())
                .phone(admin.getPhone())
                .lastName(admin.getLastName())
                .firstName(admin.getFirstName())
                .email(admin.getEmail())
                .password(passwordEncoder.encode(admin.getPassword()))
                .roles(Collections.singletonList(adminRole))
                .createdAt(LocalDateTime.now())
                .build();
        userRepository.save(adminRegister);
        return UserResponse.builder()
                .address(adminRegister.getAddress())
                .phone(adminRegister.getPhone())
                .email(adminRegister.getEmail())
                .lastName(adminRegister.getLastName())
                .firstName(adminRegister.getFirstName())
                .roles(Collections.singletonList(adminRole))
                .build();
    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if (theUser != null) {
            userRepository.deleteByEmail(email);
        }

    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}