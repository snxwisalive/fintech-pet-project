package com.example.fintech.service;

import com.example.fintech.DTO.CardCreationDTO;
import com.example.fintech.DTO.CardDTO;
import com.example.fintech.mapper.CardMapper;
import com.example.fintech.model.Card;
import com.example.fintech.model.User;
import com.example.fintech.repository.CardRepository;
import com.example.fintech.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CardMapper cardMapper;

    private CardService cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardService(cardRepository, userRepository, cardMapper);
    }

    @Test
    void createCard_shouldReturnMappedCard() {
        UUID userId = UUID.randomUUID();
        CardCreationDTO request = new CardCreationDTO(userId);
        User user = User.builder().id(userId).build();

        CardDTO expected = CardDTO.builder()
                .number("4141410000000003")
                .expirationDate(LocalDate.now().plusYears(4))
                .balance(BigDecimal.ZERO)
                .userId(userId)
                .build();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(cardRepository.existsByNumber(anyString())).thenReturn(false);
        when(cardRepository.save(any(Card.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cardMapper.toDto(any(Card.class))).thenReturn(expected);

        CardDTO result = cardService.createCard(request);

        ArgumentCaptor<Card> cardCaptor = ArgumentCaptor.forClass(Card.class);
        verify(cardRepository).save(cardCaptor.capture());
        Card saved = cardCaptor.getValue();

        assertEquals(user, saved.getUser());
        assertEquals(BigDecimal.ZERO, saved.getBalance());
        assertEquals(LocalDate.now().plusYears(4), saved.getExpirationDate());
        assertEquals(16, saved.getNumber().length());
        assertEquals(expected, result);
    }
}
