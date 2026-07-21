package com.example.fintech.controller;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.fintech.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.fintech.DTO.DepositRequestDTO;
import com.example.fintech.DTO.TransferRequestDTO;
import com.example.fintech.security.JwtService;


@WebMvcTest(TransactionController.class)
public class TransactionControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private TransactionService transactionService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    @Test
    void deposit_shouldReturn200() throws Exception {
        String toCardNumber = "4242424242424242";
        BigDecimal amount = BigDecimal.valueOf(0.01);
        DepositRequestDTO depRequest = DepositRequestDTO.builder()
            .toCardNumber(toCardNumber)
            .amount(amount)
            .build();

        mockMvc.perform(post("/api/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }

    @Test
    void transfer_shouldReturn200() throws Exception {
        UUID fromCardId = UUID.randomUUID();
        String toCardNumber = "4242424242424242";
        BigDecimal amount = BigDecimal.valueOf(0.01);
        TransferRequestDTO transferRequest = TransferRequestDTO.builder()
            .fromCardId(fromCardId)
            .toCardNumber(toCardNumber)
            .amount(amount)
            .build();

        mockMvc.perform(post("/api/transactions/transfer")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transferRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN));
    }
}