package com.example.fintech.controller;

import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.fintech.service.CardService;
import com.example.fintech.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.fintech.DTO.UserCreationDTO;
import com.example.fintech.DTO.UserDTO;
import com.example.fintech.DTO.UserUpdateDTO;
import com.example.fintech.security.JwtService;

@WebMvcTest(UserController.class)
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CardService cardService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser
    void getAllUsers_shouldReturn200() throws Exception {
        when (userService.getAllUsers()).thenReturn(List.of());
        
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void getUser_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        String firstName = "Alex";
        String lastName = "Mercer";
        String phoneNumber = "1234567890";
        UserDTO user = UserDTO.builder()
            .id(id)
            .firstName(firstName)
            .lastName(lastName)
            .phoneNumber(phoneNumber)
            .build();

        when (userService.getUserById(id)).thenReturn(user);

        mockMvc.perform(get("/api/users/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void createUser_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        String firstName = "Alex";
        String lastName = "Mercer";
        String phoneNumber = "1234567890";
        String password = "qwerty12345";
        UserCreationDTO user = UserCreationDTO.builder()
            .firstName(firstName)
            .lastName(lastName)
            .phoneNumber(phoneNumber)
            .password(password)
            .build();

        UserDTO user_dto = UserDTO.builder()
            .id(id)
            .firstName(firstName)
            .lastName(lastName)
            .phoneNumber(phoneNumber)
            .build();

        when (userService.createUser(user)).thenReturn(user_dto);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        
    }

    @Test
    @WithMockUser
    void updateUser_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        String firstName = "Alex";
        String lastName = "Mercer";
        String phoneNumber = "1234567890";
        UserDTO user_dto = UserDTO.builder()
            .id(id)
            .firstName(firstName)
            .lastName(lastName)
            .phoneNumber(phoneNumber)
            .build();

        UserUpdateDTO user = UserUpdateDTO.builder()
            .firstName(firstName)
            .lastName(lastName)
            .phoneNumber(phoneNumber)
            .build();

        when (userService.updateUser(id, user)).thenReturn(user_dto);

        mockMvc.perform(patch("/api/users/" + id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void deleteUser_shouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/users/" + id))
                .andExpect(status().isNoContent());
    }
}