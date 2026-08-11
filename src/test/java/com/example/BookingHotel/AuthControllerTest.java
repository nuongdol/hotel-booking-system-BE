package com.example.BookingHotel;

import com.example.BookingHotel.controller.AuthController;
import com.example.BookingHotel.request.UserRequest;
import com.example.BookingHotel.response.UserResponse;
import com.example.BookingHotel.service.AuthServerImp;
import com.example.BookingHotel.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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

    //test register user
    @Test
    UserRequest request = new UserRequest("John", "Doe", "john@example.com", "password123");
    UserResponse responseData = new UserResponse(1L, "John", "Doe", "john@example.com");
}
