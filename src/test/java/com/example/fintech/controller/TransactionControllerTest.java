package com.example.fintech.controller;

import com.example.fintech.DTO.DepositRequestDTO;
import com.example.fintech.DTO.TransferRequestDTO;
import com.example.fintech.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.UUID;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@WithMockUser
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TransactionService transactionService;

    @Test
    void deposit_shouldReturn200() throws Exception {
        DepositRequestDTO request = DepositRequestDTO.builder()
                .toCardNumber("4242424242424242")
                .amount(BigDecimal.valueOf(0.01))
                .build();

        mockMvc.perform(post("/api/transactions/deposit")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Deposit Successful !"));
    }

    @Test
    void transfer_shouldReturn200() throws Exception {
        TransferRequestDTO request = TransferRequestDTO.builder()
                .fromCardId(UUID.randomUUID())
                .toCardNumber("4242424242424242")
                .amount(BigDecimal.valueOf(0.01))
                .build();

        mockMvc.perform(post("/api/transactions/transfer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.TEXT_PLAIN))
                .andExpect(content().string("Transfer Successful !"));
    }

    @Test
    void transfer_shouldReturn400WhenCardNumberIsInvalid() throws Exception {
        TransferRequestDTO request = TransferRequestDTO.builder()
                .fromCardId(UUID.randomUUID())
                .toCardNumber("tea ceremony")
                .amount(BigDecimal.valueOf(500))
                .build();

        mockMvc.perform(post("/api/transactions/transfer")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
