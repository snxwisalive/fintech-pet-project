package com.example.fintech.controller;

import com.example.fintech.DTO.CardDTO;
import com.example.fintech.service.UserService;
import com.example.fintech.service.CardService;
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
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
	private MockMvc mockMvc;

	@Mock
    private UserService userService;

    @Mock
    private CardService cardService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void shouldReturnUserCards() throws Exception {
    	UUID userId = UUID.randomUUID();
		UUID cardId = UUID.randomUUID();

		CardDTO card = CardDTO.builder()
			.id(cardId)
			.number("1234567812345678")
			.balance(BigDecimal.ZERO)
			.expirationDate(LocalDate.now().plusYears(4))
			.userId(userId)
			.build();

		List<CardDTO> expectedCards = List.of(card);

		when(cardService.getAllUserCards(userId)).thenReturn(expectedCards);

		mockMvc.perform(get("/api/users/" + userId + "/cards"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[0].id").value(cardId.toString()));
    }
}