package com.bank.transaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.bank.transaction.model.dto.PageableResponse;
import com.bank.transaction.model.dto.TransactionRequest;
import com.bank.transaction.model.dto.TransactionResponse;
import com.bank.transaction.service.TransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Transaction Controller Test Class
 * 
 * @author YUNING TAO
 */
@WebMvcTest(TransactionController.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    private TransactionRequest validRequest;
    private TransactionResponse validResponse;

    @BeforeEach
    void setUp() {
        validRequest = new TransactionRequest(
                100.00,
                "USD",
                "DEPOSIT",
                "REF001"
        );

        validResponse = new TransactionResponse(
                "test-id",
                100.00,
                "USD",
                "DEPOSIT",
                "REF001",
                LocalDateTime.now()
        );
    }

    @Test
    void createTransaction_ValidRequest_ShouldReturnCreated() throws Exception {
        // Given
        when(transactionService.createTransaction(any(TransactionRequest.class)))
                .thenReturn(validResponse);

        // When & Then
        mockMvc.perform(post("/bank/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("test-id")))
                .andExpect(jsonPath("$.amount", is(100.00)))
                .andExpect(jsonPath("$.currency", is("USD")))
                .andExpect(jsonPath("$.transactionType", is("DEPOSIT")))
                .andExpect(jsonPath("$.transactionReference", is("REF001")));

        verify(transactionService).createTransaction(any(TransactionRequest.class));
    }

    @Test
    void getTransaction_ExistingId_ShouldReturnTransaction() throws Exception {
        // Given
        when(transactionService.getTransactionById("test-id")).thenReturn(validResponse);

        // When & Then
        mockMvc.perform(get("/bank/transactions/test-id"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("test-id")))
                .andExpect(jsonPath("$.amount", is(100.00)))
                .andExpect(jsonPath("$.currency", is("USD")));

        verify(transactionService).getTransactionById("test-id");
    }

    @Test
    void getTransactions_DefaultPagination_ShouldReturnPagedResponse() throws Exception {
        // Given
        PageableResponse<TransactionResponse> pageableResponse = new PageableResponse<>(
                Arrays.asList(validResponse),
                0,
                10,
                1L
        );
        when(transactionService.getTransactions(0, 10)).thenReturn(pageableResponse);

        // When & Then
        mockMvc.perform(get("/bank/transactions"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id", is("test-id")))
                .andExpect(jsonPath("$.page", is(0)))
                .andExpect(jsonPath("$.size", is(10)))
                .andExpect(jsonPath("$.totalElements", is(1)))
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.first", is(true)))
                .andExpect(jsonPath("$.last", is(true)));

        verify(transactionService).getTransactions(0, 10);
    }

    @Test
    void getTransactions_CustomPagination_ShouldReturnPagedResponse() throws Exception {
        // Given
        PageableResponse<TransactionResponse> pageableResponse = new PageableResponse<>(
                Arrays.asList(validResponse),
                1,
                5,
                6L
        );
        when(transactionService.getTransactions(1, 5)).thenReturn(pageableResponse);

        // When & Then
        mockMvc.perform(get("/bank/transactions")
                        .param("page", "1")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.page", is(1)))
                .andExpect(jsonPath("$.size", is(5)))
                .andExpect(jsonPath("$.totalElements", is(6)))
                .andExpect(jsonPath("$.totalPages", is(2)));

        verify(transactionService).getTransactions(1, 5);
    }

    @Test
    void updateTransaction_ValidRequest_ShouldReturnUpdatedTransaction() throws Exception {
        // Given
        TransactionResponse updatedResponse = new TransactionResponse(
                "test-id",
                200.00,
                "EUR",
                "WITHDRAWAL",
                "REF002",
                LocalDateTime.now()
        );
        when(transactionService.updateTransaction(eq("test-id"), any(TransactionRequest.class)))
                .thenReturn(updatedResponse);

        TransactionRequest updateRequest = new TransactionRequest(
                200.00,
                "EUR",
                "WITHDRAWAL",
                "REF002"
        );

        // When & Then
        mockMvc.perform(put("/bank/transactions/test-id")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is("test-id")))
                .andExpect(jsonPath("$.amount", is(200.00)))
                .andExpect(jsonPath("$.currency", is("EUR")))
                .andExpect(jsonPath("$.transactionType", is("WITHDRAWAL")))
                .andExpect(jsonPath("$.transactionReference", is("REF002")));

        verify(transactionService).updateTransaction(eq("test-id"), any(TransactionRequest.class));
    }

    @Test
    void deleteTransaction_ExistingId_ShouldReturnNoContent() throws Exception {
        // Given
        doNothing().when(transactionService).deleteTransaction("test-id");

        // When & Then
        mockMvc.perform(delete("/bank/transactions/test-id"))
                .andExpect(status().isNoContent());

        verify(transactionService).deleteTransaction("test-id");
    }

} 