package com.bank.transaction.service;

import com.bank.transaction.model.dto.PageableResponse;
import com.bank.transaction.model.dto.TransactionRequest;
import com.bank.transaction.model.dto.TransactionResponse;
import com.bank.transaction.exception.TransactionException;
import com.bank.transaction.model.Transaction;
import com.bank.transaction.dao.TransactionDao;
import com.bank.transaction.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Transaction Service Test Class
 * 
 * @author YUNING TAO
 */
@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private TransactionDao transactionDao;

    @InjectMocks
    private TransactionServiceImpl transactionService;

    private TransactionRequest validRequest;
    private Transaction validTransaction;

    @BeforeEach
    void setUp() {
        validRequest = new TransactionRequest(
                100.00,
                "USD",
                "DEPOSIT",
                "REF001"
        );

        validTransaction = new Transaction(
                100.00,
                "USD",
                "DEPOSIT",
                "REF001"
        );
        validTransaction.setId("test-id");
        validTransaction.setTimestamp(LocalDateTime.now());
    }

    @Test
    void createTransaction_ValidRequest_ShouldReturnTransactionResponse() {
        // Given
        when(transactionDao.existsByTransactionReference("REF001")).thenReturn(false);
        when(transactionDao.save(any(Transaction.class))).thenReturn(validTransaction);

        // When
        TransactionResponse response = transactionService.createTransaction(validRequest);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("test-id");
        assertThat(response.getAmount()).isEqualTo(100.00);
        assertThat(response.getCurrency()).isEqualTo("USD");
        assertThat(response.getTransactionType()).isEqualTo("DEPOSIT");

        verify(transactionDao).existsByTransactionReference("REF001");
        verify(transactionDao).save(any(Transaction.class));
    }

    @Test
    void createTransaction_DuplicateReferenceNumber_ShouldThrowException() {
        // Given
        when(transactionDao.existsByTransactionReference("REF001")).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> transactionService.createTransaction(validRequest))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining("REF001");

        verify(transactionDao).existsByTransactionReference("REF001");
        verify(transactionDao, never()).save(any());
    }

    @Test
    void createTransaction_InvalidAmount_ShouldThrowException() {
        // Given
        TransactionRequest invalidRequest = new TransactionRequest(
                -10.00,
                "USD",
                "DEPOSIT",
                "REF001"
        );

        // When & Then
        assertThatThrownBy(() -> transactionService.createTransaction(invalidRequest))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining("Invalid Transaction Amount，Must be over 0");
    }

    @Test
    void createTransaction_InvalidCurrency_ShouldThrowException() {
        // Given
        TransactionRequest invalidRequest = new TransactionRequest(
                100.00,
                "XXX",
                "DEPOSIT",
                "REF001"
        );

        // When & Then
        assertThatThrownBy(() -> transactionService.createTransaction(invalidRequest))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining("Invalid Currency:");
    }

    @Test
    void createTransaction_InvalidTransactionType_ShouldThrowException() {
        // Given
        TransactionRequest invalidRequest = new TransactionRequest(
                100.00,
                "USD",
                "INVALID_TYPE",
                "REF001"
        );

        // When & Then
        assertThatThrownBy(() -> transactionService.createTransaction(invalidRequest))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining("Invalid Transaction Type: INVALID_TYPE");
    }

    @Test
    void getTransactionById_ExistingId_ShouldReturnTransaction() {
        // Given
        when(transactionDao.findById("test-id")).thenReturn(Optional.of(validTransaction));

        // When
        TransactionResponse response = transactionService.getTransactionById("test-id");

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getId()).isEqualTo("test-id");
        assertThat(response.getAmount()).isEqualTo(100.00);

        verify(transactionDao).findById("test-id");
    }

    @Test
    void getTransactionById_NonExistingId_ShouldThrowException() {
        // Given
        when(transactionDao.findById("non-existing")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> transactionService.getTransactionById("non-existing"))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining("non-existing");

        verify(transactionDao).findById("non-existing");
    }

    @Test
    void getTransactionById_EmptyId_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> transactionService.getTransactionById(""))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining("Transaction ID cannot be empty.");
    }

    @Test
    void getTransactions_ValidPageAndSize_ShouldReturnPagedResponse() {
        // Given
        when(transactionDao.findAll(0, 10)).thenReturn(Arrays.asList(validTransaction));
        when(transactionDao.count()).thenReturn(1L);

        // When
        PageableResponse<TransactionResponse> response = transactionService.getTransactions(0, 10);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.getContent()).hasSize(1);
        assertThat(response.getPage()).isEqualTo(0);
        assertThat(response.getSize()).isEqualTo(10);
        assertThat(response.getTotalElements()).isEqualTo(1L);
        assertThat(response.getTotalPages()).isEqualTo(1);

        verify(transactionDao).findAll(0, 10);
        verify(transactionDao).count();
    }

    @Test
    void getTransactions_InvalidPageSize_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> transactionService.getTransactions(0, 0))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining("Page size should be between 1 to 100.");

        assertThatThrownBy(() -> transactionService.getTransactions(0, 101))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining("Page size should be between 1 to 100.");
    }

    @Test
    void getTransactions_InvalidPageNumber_ShouldThrowException() {
        // When & Then
        assertThatThrownBy(() -> transactionService.getTransactions(-1, 10))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining("page Number should not be less than 0.");
    }

    @Test
    void updateTransaction_NonExistingId_ShouldThrowException() {
        // Given
        when(transactionDao.findById("non-existing")).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> transactionService.updateTransaction("non-existing", validRequest))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining("non-existing");

        verify(transactionDao).findById("non-existing");
        verify(transactionDao, never()).save(any());
    }

    @Test
    void deleteTransaction_ExistingId_ShouldDeleteSuccessfully() {
        // Given
        when(transactionDao.existsById("test-id")).thenReturn(true);
        when(transactionDao.deleteById("test-id")).thenReturn(true);

        // When
        transactionService.deleteTransaction("test-id");

        // Then
        verify(transactionDao).existsById("test-id");
        verify(transactionDao).deleteById("test-id");
    }

    @Test
    void deleteTransaction_NonExistingId_ShouldThrowException() {
        // Given
        when(transactionDao.existsById("non-existing")).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> transactionService.deleteTransaction("non-existing"))
                .isInstanceOf(TransactionException.class)
                .hasMessageContaining("non-existing");

        verify(transactionDao).existsById("non-existing");
        verify(transactionDao, never()).deleteById(any());
    }

    @Test
    void existsById_ExistingId_ShouldReturnTrue() {
        // Given
        when(transactionDao.existsById("test-id")).thenReturn(true);

        // When
        boolean exists = transactionService.existsById("test-id");

        // Then
        assertThat(exists).isTrue();
        verify(transactionDao).existsById("test-id");
    }

    @Test
    void existsById_NonExistingId_ShouldReturnFalse() {
        // Given
        when(transactionDao.existsById("non-existing")).thenReturn(false);

        // When
        boolean exists = transactionService.existsById("non-existing");

        // Then
        assertThat(exists).isFalse();
        verify(transactionDao).existsById("non-existing");
    }
} 