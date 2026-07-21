package com.example.fintech.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.never;
import java.util.UUID;
import java.math.BigDecimal;
import java.util.Optional;

import com.example.fintech.model.Card;
import com.example.fintech.repository.CardRepository;
import com.example.fintech.DTO.TransferRequestDTO;
import com.example.fintech.DTO.DepositRequestDTO;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceTest {
	@Mock
	private CardRepository cardRepository;

	@InjectMocks
	private TransactionService transactionService;

	@Test
	public void shouldTransferSuccessfully() {
		UUID senderCardId = UUID.randomUUID();
		UUID receiverCardId = UUID.randomUUID();

		String receiverPan = "4242424242424242";

		Card senderCard = Card.builder()
				.id(senderCardId)
				.balance(BigDecimal.valueOf(1500))
				.build();

		Card receiverCard = Card.builder()
				.id(receiverCardId)
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
	public void shouldThrowExceptionWhenNotEnoughMoney() {
		UUID senderCardId = UUID.randomUUID();
		UUID receiverCardId = UUID.randomUUID();

		String receiverPan = "4242424242424242";

		Card senderCard = Card.builder()
				.id(senderCardId)
				.balance(BigDecimal.valueOf(200))
				.build();

		Card receiverCard = Card.builder()
				.id(receiverCardId)
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

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			transactionService.transfer(request);
		});

		verify(cardRepository, never()).save(any(Card.class));

		assertEquals("Not enough money", exception.getMessage());
	}

	@Test
	public void shouldThrowExceptionReceiverNotFound() {
		UUID senderCardId = UUID.randomUUID();
		UUID receiverCardId = UUID.randomUUID();

		String receiverPan = "4242424242424242";
		String wrongPan = "4141410000000003";


		Card senderCard = Card.builder()
				.id(senderCardId)
				.balance(BigDecimal.valueOf(200))
				.build();

		Card receiverCard = Card.builder()
				.id(receiverCardId)
				.number(wrongPan)
				.balance(BigDecimal.valueOf(100))
				.build();

		TransferRequestDTO request = TransferRequestDTO.builder()
							.fromCardId(senderCardId)
							.toCardNumber(wrongPan)
							.amount(BigDecimal.valueOf(10))
							.build();

		when(cardRepository.findById(senderCardId)).thenReturn(Optional.of(senderCard));
		when(cardRepository.findByNumber(wrongPan)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			transactionService.transfer(request);
		});

		verify(cardRepository, never()).save(any(Card.class));

		assertEquals("Resource not found in the database", exception.getMessage());
	}

	@Test
	public void shouldDepositSuccessfully() {
		UUID receiverCardId = UUID.randomUUID();

		String receiverPan = "4242424242424242";

		Card receiverCard = Card.builder()
				.id(receiverCardId)
				.number(receiverPan)
				.balance(BigDecimal.valueOf(0))
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
	public void shouldThrowExceptionCardNotFoundWhileDepositing() {
		UUID receiverCardId = UUID.randomUUID();

		String receiverPan = "4242424242424242";
		String wrongPan = "4141410000000003";

		Card receiverCard = Card.builder()
				.id(receiverCardId)
				.number(receiverPan)
				.balance(BigDecimal.valueOf(0))
				.build();

		DepositRequestDTO request = DepositRequestDTO.builder()
							.toCardNumber(wrongPan)
							.amount(BigDecimal.valueOf(100))
							.build();

		when(cardRepository.findByNumber(wrongPan)).thenReturn(Optional.empty());

		RuntimeException exception = assertThrows(RuntimeException.class, () -> {
			transactionService.deposit(request);
		});

		verify(cardRepository, never()).save(any(Card.class));

		assertEquals("Resource not found in the database", exception.getMessage());
	}
}