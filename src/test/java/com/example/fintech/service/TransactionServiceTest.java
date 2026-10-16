package com.example.fintech.service;

import com.example.fintech.DTO.DepositRequestDTO;
import com.example.fintech.DTO.TransferRequestDTO;
import com.example.fintech.exception.InsufficientFundsException;
import com.example.fintech.exception.ResourceNotFoundException;
import com.example.fintech.model.Card;
import com.example.fintech.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private CardRepository cardRepository;

    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService(cardRepository);
    }

    @Test
    void transfer_shouldMoveFundsBetweenCards() {
        UUID senderCardId = UUID.randomUUID();
        String receiverPan = "4242424242424242";

        Card senderCard = Card.builder()
                .id(senderCardId)
                .balance(BigDecimal.valueOf(1500))
                .build();

        Card receiverCard = Card.builder()
                .id(UUID.randomUUID())
                .number(receiverPan)
                .balance(BigDecimal.valueOf(100))
                .build();

        TransferRequestDTO request = TransferRequestDTO.builder()
                .fromCardId(senderCardId)
                .toCardNumber(receiverPan)
                .amount(BigDecimal.valueOf(500))
                .build();

        when(cardRepository.findById(senderCardId)).thenReturn(Optional.of(senderCard));
        when(cardRepository.findByNumber(receiverPan)).thenReturn(Optional.of(receiverCard));

        transactionService.transfer(request);

        verify(cardRepository).save(senderCard);
        verify(cardRepository).save(receiverCard);
        assertEquals(BigDecimal.valueOf(1000), senderCard.getBalance());
        assertEquals(BigDecimal.valueOf(600), receiverCard.getBalance());
    }

    @Test
    void transfer_shouldThrowWhenInsufficientFunds() {
        UUID senderCardId = UUID.randomUUID();
        String receiverPan = "4242424242424242";

        Card senderCard = Card.builder()
                .id(senderCardId)
                .balance(BigDecimal.valueOf(200))
                .build();

        Card receiverCard = Card.builder()
                .id(UUID.randomUUID())
                .number(receiverPan)
                .balance(BigDecimal.valueOf(100))
                .build();

        TransferRequestDTO request = TransferRequestDTO.builder()
                .fromCardId(senderCardId)
                .toCardNumber(receiverPan)
                .amount(BigDecimal.valueOf(500))
                .build();

        when(cardRepository.findById(senderCardId)).thenReturn(Optional.of(senderCard));
        when(cardRepository.findByNumber(receiverPan)).thenReturn(Optional.of(receiverCard));

        InsufficientFundsException exception = assertThrows(InsufficientFundsException.class,
                () -> transactionService.transfer(request));

        verify(cardRepository, never()).save(any(Card.class));
        assertEquals("Not enough money on the card", exception.getMessage());
    }

    @Test
    void transfer_shouldThrowWhenReceiverNotFound() {
        UUID senderCardId = UUID.randomUUID();
        String missingPan = "4141410000000003";

        Card senderCard = Card.builder()
                .id(senderCardId)
                .balance(BigDecimal.valueOf(200))
                .build();

        TransferRequestDTO request = TransferRequestDTO.builder()
                .fromCardId(senderCardId)
                .toCardNumber(missingPan)
                .amount(BigDecimal.valueOf(10))
                .build();

        when(cardRepository.findById(senderCardId)).thenReturn(Optional.of(senderCard));
        when(cardRepository.findByNumber(missingPan)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> transactionService.transfer(request));

        verify(cardRepository, never()).save(any(Card.class));
        assertEquals("Receiver card not found", exception.getMessage());
    }

    @Test
    void deposit_shouldAddAmountToCard() {
        String receiverPan = "4242424242424242";
        Card receiverCard = Card.builder()
                .id(UUID.randomUUID())
                .number(receiverPan)
                .balance(BigDecimal.ZERO)
                .build();

        DepositRequestDTO request = DepositRequestDTO.builder()
                .toCardNumber(receiverPan)
                .amount(BigDecimal.valueOf(100))
                .build();

        when(cardRepository.findByNumber(receiverPan)).thenReturn(Optional.of(receiverCard));

        transactionService.deposit(request);

        verify(cardRepository).save(receiverCard);
        assertEquals(BigDecimal.valueOf(100), receiverCard.getBalance());
    }

    @Test
    void deposit_shouldThrowWhenCardNotFound() {
        String missingPan = "4141410000000003";
        DepositRequestDTO request = DepositRequestDTO.builder()
                .toCardNumber(missingPan)
                .amount(BigDecimal.valueOf(100))
                .build();

        when(cardRepository.findByNumber(missingPan)).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> transactionService.deposit(request));

        verify(cardRepository, never()).save(any(Card.class));
        assertEquals("Receiver card not found", exception.getMessage());
    }
}
