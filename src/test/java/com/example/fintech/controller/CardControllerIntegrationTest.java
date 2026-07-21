package com.example.fintech.controller;

import java.math.BigDecimal;
import java.time.LocalDate;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.test.context.support.WithMockUser;

import com.example.fintech.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.fintech.DTO.CardCreationDTO;
import com.example.fintech.DTO.CardDTO;
import com.example.fintech.security.JwtService;

@WebMvcTest(CardController.class)
public class CardControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CardService cardService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    @WithMockUser
    void createCard_shouldReturn201() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        String number = "4242424242424242";
        LocalDate expirationDate = LocalDate.now().plusYears(4);
        BigDecimal balance = BigDecimal.ZERO;

        CardCreationDTO card = CardCreationDTO.builder()
            .userId(userId)
            .build();

        CardDTO card_dto = CardDTO.builder()
            .id(id)
            .number(number)
            .expirationDate(expirationDate)
            .balance(balance)
            .userId(userId)
            .build();

        when (cardService.createCard(card)).thenReturn(card_dto);

        mockMvc.perform(post("/api/cards")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(card)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void getCard_shouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        String number = "4242424242424242";
        LocalDate expirationDate = LocalDate.now().plusYears(4);
        BigDecimal balance = BigDecimal.ZERO;

        CardDTO card_dto = CardDTO.builder()
            .id(id)
            .number(number)
            .expirationDate(expirationDate)
            .balance(balance)
            .userId(userId)
            .build();

        when (cardService.getCardById(id)).thenReturn(card_dto);

        mockMvc.perform(get("/api/cards/" + id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void deleteCard_shouldReturn204() throws Exception {
        UUID id = UUID.randomUUID();

        mockMvc.perform(delete("/api/cards/" + id))
                .andExpect(status().isNoContent());                
    }
}