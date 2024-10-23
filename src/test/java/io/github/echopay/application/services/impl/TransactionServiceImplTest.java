package io.github.echopay.application.services.impl;

import io.github.echopay.application.dtos.inputs.TransactionInputDTO;
import io.github.echopay.application.dtos.outputs.TransactionOutputDTO;
import io.github.echopay.domain.enums.PaymentMethod;
import io.github.echopay.domain.enums.TransactionStatus;
import io.github.echopay.domain.models.Transaction;
import io.github.echopay.infrastructure.exceptions.TransactionNotFoundException;
import io.github.echopay.infrastructure.repositories.TransactionRepository;
import io.github.echopay.utils.UUIDGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UUIDGenerator uuidGenerator;

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateTransactionAndSendToKafka() {
        // Arrange
        TransactionInputDTO inputDTO = new TransactionInputDTO();
        inputDTO.setAmount(new BigDecimal("100.00"));
        inputDTO.setPaymentMethod(PaymentMethod.PIX);
        inputDTO.setCurrencyCode("BRL");

        Transaction transaction = new Transaction();
        UUID uuid = UUID.randomUUID();
        transaction.setUuid(uuid);
        transaction.setAmount(inputDTO.getAmount());
        transaction.setPaymentMethod(inputDTO.getPaymentMethod());
        transaction.setCurrencyCode(inputDTO.getCurrencyCode());
        transaction.setStatus(TransactionStatus.PENDING);

        when(modelMapper.map(any(TransactionInputDTO.class), eq(Transaction.class))).thenReturn(transaction);
        when(uuidGenerator.generateUUID()).thenReturn(uuid);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        UUID result = transactionService.createTransaction(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(uuid, result);
        verify(transactionRepository, times(1)).save(transaction);
        verify(kafkaTemplate, times(1)).send(eq("transactions"), eq(transaction.getUuid().toString()), eq(transaction));
    }

    @Test
    void shouldFindTransactionByUuid() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Transaction transaction = new Transaction();
        transaction.setUuid(uuid);
        TransactionOutputDTO outputDTO = new TransactionOutputDTO();
        outputDTO.setUuid(uuid.toString());

        when(transactionRepository.findById(uuid)).thenReturn(Optional.of(transaction));
        when(modelMapper.map(transaction, TransactionOutputDTO.class)).thenReturn(outputDTO);

        // Act
        TransactionOutputDTO result = transactionService.getTransactionByUuid(uuid);

        // Assert
        assertNotNull(result);
        assertEquals(uuid.toString(), result.getUuid());
        verify(transactionRepository, times(1)).findById(uuid);
    }

    @Test
    void shouldThrowExceptionWhenTransactionNotFound() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(transactionRepository.findById(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransactionNotFoundException.class, () -> transactionService.getTransactionByUuid(uuid));
        verify(transactionRepository, times(1)).findById(uuid);
    }

    @Test
    void shouldUpdateTransactionStatus() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        Transaction transaction = new Transaction();
        transaction.setUuid(uuid);
        transaction.setStatus(TransactionStatus.PENDING);

        when(transactionRepository.findById(uuid)).thenReturn(Optional.of(transaction));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        // Act
        transactionService.updateTransactionStatus(uuid, TransactionStatus.FINISHED);

        // Assert
        assertEquals(TransactionStatus.FINISHED, transaction.getStatus());
        verify(transactionRepository, times(1)).findById(uuid);
        verify(transactionRepository, times(1)).save(transaction);
    }

    @Test
    void shouldThrowTransactionNotFoundExceptionOnUpdate() {
        // Arrange
        UUID uuid = UUID.randomUUID();
        when(transactionRepository.findById(uuid)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(TransactionNotFoundException.class, () -> transactionService.updateTransactionStatus(uuid, TransactionStatus.FINISHED));
        verify(transactionRepository, times(1)).findById(uuid);
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void shouldThrowDataIntegrityViolationExceptionOnCreate() {
        // Arrange
        TransactionInputDTO inputDTO = new TransactionInputDTO();
        inputDTO.setAmount(new BigDecimal("100.00"));
        inputDTO.setPaymentMethod(PaymentMethod.PIX);
        inputDTO.setCurrencyCode("BRL");

        Transaction transaction = new Transaction();
        UUID uuid = UUID.randomUUID();
        transaction.setUuid(uuid);

        when(modelMapper.map(any(TransactionInputDTO.class), eq(Transaction.class))).thenReturn(transaction);
        when(uuidGenerator.generateUUID()).thenReturn(uuid);
        when(transactionRepository.save(any(Transaction.class))).thenThrow(DataIntegrityViolationException.class);

        // Act & Assert
        assertThrows(DataIntegrityViolationException.class, () -> transactionService.createTransaction(inputDTO));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

}
