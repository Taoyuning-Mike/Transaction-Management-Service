package com.bank.transaction.controller;

import com.bank.transaction.model.dto.PageableResponse;
import com.bank.transaction.model.dto.TransactionRequest;
import com.bank.transaction.model.dto.TransactionResponse;
import com.bank.transaction.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Transaction controller
 *
 * @author YUNING TAO
 */
@RestController
@RequestMapping("/bank/transactions")
@Tag(name = "Transaction management", description = "Transaction management services")
public class TransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @Operation(summary = "Create Transaction", description = "Create Transaction")
    public ResponseEntity<TransactionResponse> createTransaction(
            @Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.createTransaction(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update Transaction", description = "update Transaction")
    public ResponseEntity<TransactionResponse> updateTransaction(
            @Parameter(required = true)
            @PathVariable String id,
            @Valid @RequestBody TransactionRequest request) {
        TransactionResponse response = transactionService.updateTransaction(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "deleteTransaction", description = "delete Transaction By ID")
    public ResponseEntity<Void> deleteTransaction(
            @Parameter(required = true)
            @PathVariable String id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @Operation(summary = "get Transaction", description = "get Transaction By ID")
    public ResponseEntity<TransactionResponse> getTransaction(
            @Parameter(required = true)
            @PathVariable String id) {
        TransactionResponse response = transactionService.getTransactionById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "get Transactions", description = "get Transactions with pagination result")
    public ResponseEntity<PageableResponse<TransactionResponse>> getTransactions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PageableResponse<TransactionResponse> response = transactionService.getTransactions(page, size);
        return ResponseEntity.ok(response);
    }
} 