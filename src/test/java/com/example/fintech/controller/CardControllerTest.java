package com.example.fintech.controller;

import com.example.fintech.DTO.CardCreationDTO;
import com.example.fintech.DTO.CardDTO;
import com.example.fintech.service.CardService;
import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CardControllerTest {
	private MockMvc mockMvc;

	private ObjectMapper objectMapper = new ObjectMapper();

	@Mock
    private CardService cardService;

    @InjectMocks
    private CardController cardController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(cardController).build();
    }

	@Test
	public void shouldCreateCardAndReturn201() throws Exception {
		UUID userId = UUID.randomUUID();
		UUID cardId = UUID.randomUUID();

		CardCreationDTO requestDto = new CardCreationDTO(userId);

		CardDTO responseDto = CardDTO.builder()
			.id(cardId)
			.number("4242424242424242")
			.balance(BigDecimal.ZERO)
			.expirationDate(LocalDate.now().plusYears(4))
			.userId(userId)
			.build();

		when(cardService.createCard(any(CardCreationDTO.class))).thenReturn(responseDto);

		mockMvc.perform(post("/api/cards")
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(requestDto)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id").value(cardId.toString()))
				.andExpect(jsonPath("$.number").value("4242424242424242"));
	}
}