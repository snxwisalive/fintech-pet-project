package com.example.fintech.controller;

import com.example.fintech.DTO.CardCreationDTO;
import com.example.fintech.DTO.CardDTO;
import com.example.fintech.service.CardService;
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
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CardController.class)
@WithMockUser
class CardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CardService cardService;

    @Test
    void createCard_shouldReturn201() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        CardCreationDTO request = CardCreationDTO.builder()
                .userId(userId)
                .build();

        CardDTO response = CardDTO.builder()
                .id(cardId)
                .number("4242424242424242")
                .expirationDate(LocalDate.now().plusYears(4))
                .balance(BigDecimal.ZERO)
                .userId(userId)
                .build();

        when(cardService.createCard(request)).thenReturn(response);

        mockMvc.perform(post("/api/cards")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cardId.toString()))
                .andExpect(jsonPath("$.number").value("4242424242424242"));
    }

    @Test
    void getCardById_shouldReturn200() throws Exception {
        UUID userId = UUID.randomUUID();
        UUID cardId = UUID.randomUUID();
        CardDTO response = CardDTO.builder()
                .id(cardId)
                .number("4242424242424242")
                .expirationDate(LocalDate.now().plusYears(4))
                .balance(BigDecimal.ZERO)
                .userId(userId)
                .build();

        when(cardService.getCardById(cardId)).thenReturn(response);

        mockMvc.perform(get("/api/cards/" + cardId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(cardId.toString()));
    }

    @Test
    void deleteCard_shouldReturn204() throws Exception {
        UUID cardId = UUID.randomUUID();

        mockMvc.perform(delete("/api/cards/" + cardId).with(csrf()))
                .andExpect(status().isNoContent());
    }
}
