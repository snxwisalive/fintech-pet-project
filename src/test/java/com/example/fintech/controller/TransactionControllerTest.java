package com.example.fintech.controller;

import com.example.fintech.DTO.TransferRequestDTO;
import com.example.fintech.service.TransactionService;
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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TransactionControllerTest {
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private TransactionService transactionService;

    @InjectMocks
	private TransactionController transactionController;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();
    }

    @Test
    public void shouldReturn400WhenCardNumberIsInvalid() throws Exception {
    	UUID senderCardId = UUID.randomUUID();

    	TransferRequestDTO request = TransferRequestDTO.builder()
							.fromCardId(senderCardId)
							.toCardNumber("tea ceremony")
							.amount(BigDecimal.valueOf(500))
							.build();

		mockMvc.perform(post("/api/transactions/transfer")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)))
        		.andExpect(status().isBadRequest());
    }
}