package com.example.fintech.controller;

import com.example.fintech.DTO.CardDTO;
import com.example.fintech.DTO.UserCreationDTO;
import com.example.fintech.DTO.UserDTO;
import com.example.fintech.DTO.UserUpdateDTO;
import com.example.fintech.service.CardService;
import com.example.fintech.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@WithMockUser
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private CardService cardService;

    @Test
    void getAllUsers_shouldReturn200() throws Exception {
        when(userService.getAllUsers()).thenReturn(List.of());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void getUserById_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        UserDTO user = UserDTO.builder()
                .id(id)
                .firstName("Alex")
                .lastName("Mercer")
                .phoneNumber("1234567890")
                .build();

        when(userService.getUserById(id)).thenReturn(user);

        mockMvc.perform(get("/api/users/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.firstName").value("Alex"));
    }

    @Test
    void createUser_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        UserCreationDTO request = UserCreationDTO.builder()
                .firstName("Alex")
                .lastName("Mercer")
                .email("alex.mercer@example.com")
                .phoneNumber("1234567890")
                .password("qwerty12345")
                .build();

        UserDTO response = UserDTO.builder()
                .id(id)
                .firstName("Alex")
                .lastName("Mercer")
                .phoneNumber("1234567890")
                .build();

        when(userService.createUser(request)).thenReturn(response);

        mockMvc.perform(post("/api/users")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void updateUser_shouldReturn200() throws Exception {
        UUID id = UUID.randomUUID();
        UserUpdateDTO request = UserUpdateDTO.builder()
                .firstName("Alex")
                .lastName("Mercer")
                .phoneNumber("1234567890")
                .build();

        UserDTO response = UserDTO.builder()
                .id(id)
                .firstName("Alex")
                .lastName("Mercer")
                .phoneNumber("1234567890")
                .build();

        when(userService.updateUser(id, request)).thenReturn(response);

        mockMvc.perform(patch("/api/users/" + id)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    void deleteUser_shouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/users/" + id).with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    void getAllUserCards_shouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();

        CardDTO card = CardDTO.builder()
                .id(cardId)
                .number("1234567812345678")
                .balance(BigDecimal.ZERO)
                .expirationDate(LocalDate.now().plusYears(4))
                .userId(userId)
                .build();

        when(cardService.getAllUserCards(userId)).thenReturn(List.of(card));

        mockMvc.perform(get("/api/users/" + userId + "/cards"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(cardId.toString()));
    }
}
