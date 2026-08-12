package com.example.BookingHotel;

import com.example.BookingHotel.controller.AuthController;
import com.example.BookingHotel.request.LoginRequest;
import com.example.BookingHotel.request.UserRequest;
import com.example.BookingHotel.response.JwtResponse;
import com.example.BookingHotel.response.UserResponse;
import com.example.BookingHotel.service.AuthServerImp;
import com.example.BookingHotel.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

//using JUnit5, Mockito & MockMvc
@WebMvcTest(AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @MockBean
    private AuthServerImp authServerImp;

    private UserRequest userRequest;
    private UserResponse userResponse;

    @BeforeEach
    void setUser() {
        //dung data dung chung cho nhieu test, tranh lap code.
        userRequest = new UserRequest();
        userRequest.setFirstName("Nguyen");
        userRequest.setLastName("An");
        userRequest.setEmail("annguyen@example.com");
        userRequest.setPassword("12345678");

        userResponse = new UserResponse();
        userResponse.setUserId(1L);
        userResponse.setFirstName("Nguyen");
        userResponse.setLastName("An");
        userResponse.setEmail("annguyen@example.com");
    }

    //test dang ky user ket qua thanh cong
    @Test
    @DisplayName("registerUser - Dang ky user thanh cong tra ve 201 CREATED")
    void registerUser_Success() throws Exception {
        when(userService.registerUser(any(UserRequest.class))).thenReturn(userResponse);
        mockMvc.perform(post("/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                //then (kiem tra ket qua tra ve)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("Successful Registration!"))
                .andExpect(jsonPath("$.data.email").value("an.nguyen@example.com"));
        //service gọi đúng 1 lần
        verify(userService, times(1)).registerUser(any(UserRequest.class));
    }

    //test dang ky boi user nhung sai email
    @Test
    @DisplayName("registerUser - Thieu email hop le thi tra ve 400 bad request")
    void registerUser_InvalidEmail_BadRequest() throws Exception {
        userRequest.setEmail("email");
        mockMvc.perform(post("/register-user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isBadRequest());

        verify(userService, times(0)).registerUser(any());
    }

    //test dang ky boi admin ket qua thanh cong
    @Test
    @DisplayName("registerAdmin - Dang ky admin thanh cong")
    void registerAdmin_Success() throws Exception {
        when(userService.registerAdmin(any(UserRequest.class))).thenReturn(userResponse);
        mockMvc.perform(post("/register-admin")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(201))
                .andExpect(jsonPath("$.message").value("Successful Registration!"))
                .andExpect(jsonPath("$.data.firstName").value("Nguyen"));
        verify(userService, times(1)).registerAdmin(any(UserRequest.class));
    }

    //test login ket qua thanh cong
    @Test
    @DisplayName("login-dang nhap thanh cong")
    void login_Success() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("annguyen@example.com");
        loginRequest.setPassword("Password@123");

        JwtResponse jwtResponse = new JwtResponse();
        jwtResponse.setAccessToken("fake-jwt-access-token");

        when(authServerImp.login(any(LoginRequest.class), any())).thenReturn(jwtResponse);

        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(header().string("Authorization", "Bear fake-jwt-access-token"))
                .andExpect(jsonPath("$.status").value("SUCCESS"))
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Login successful!"))
                .andExpect(jsonPath("$.data.accessToken").value("fake-jwt-access-token"));
        verify(authServerImp, times(1)).login(any(LoginRequest.class), any());
    }

    //test login -sai password
    @Test
    @DisplayName("login-password thieu tra ve 400 bad request")
    void login_MissPassword_BadRequest() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("annguyen@example.com");
        loginRequest.setPassword("");
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isBadRequest());

        verify(authServerImp, times(0)).login(any(), any());
    }
}
