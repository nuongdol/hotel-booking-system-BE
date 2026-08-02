package com.example.BookingHotel.service;

import com.example.BookingHotel.exception.UserAlreadyExistsException;
import com.example.BookingHotel.model.Role;
import com.example.BookingHotel.model.User;
import com.example.BookingHotel.repository.RoleRepository;
import com.example.BookingHotel.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public User registerUser(User user) {
        if (userRepository.existsByEmail(user.getEmail())){
            throw new UserAlreadyExistsException(user.getEmail() + " already exists");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        System.out.println(user.getPassword());
        Role userRole = roleRepository.findByName("ROLE_USER").get();
        user.setRoles(Collections.singletonList(userRole));
        return userRepository.save(user);
    }
    public User registerAdmin(User Admin){
        if(userRepository.existsByEmail(Admin.getEmail())){
            throw new UserAlreadyExistsException(Admin.getEmail() + "already exists");
        }
        Admin.setPassword(passwordEncoder.encode(Admin.getPassword()));//lay password ma hoa roi khoi tao password nay
        //cho admin
        System.out.println(Admin.getPassword());
        Role adminRole = roleRepository.findByName("ROLE_ADMIN").get();
        Admin.setRoles(Collections.singletonList(adminRole));
        return userRepository.save(Admin);

    }

    @Override
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional
    @Override
    public void deleteUser(String email) {
        User theUser = getUser(email);
        if (theUser != null){
            userRepository.deleteByEmail(email);
        }

    }

    @Override
    public User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }
}