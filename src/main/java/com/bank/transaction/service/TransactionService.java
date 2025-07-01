package com.bank.transaction.service;

import com.bank.transaction.model.dto.PageableResponse;
import com.bank.transaction.model.dto.TransactionRequest;
import com.bank.transaction.model.dto.TransactionResponse;

/**
 * Transaction Service Interface
 * 
 * @author YUNING TAO
 */
public interface TransactionService {

    /**
     * Create Transaction
     * 
     * @param request Transaction request
     * @return Transaction response
     */
    TransactionResponse createTransaction(TransactionRequest request);

    /**
     * Get transaction by ID
     * 
     * @param id Transaction ID
     * @return Transaction response
     */
    TransactionResponse getTransactionById(String id);

    /**
     * Get Pageable Transaction list
     * 
     * @param page page number
     * @param size page size
     * @return pageable response
     */
    PageableResponse<TransactionResponse> getTransactions(int page, int size);

    /**
     * Update Transaction
     * 
     * @param id Transaction ID
     * @param request Update request
     * @return Transaction response
     */
    TransactionResponse updateTransaction(String id, TransactionRequest request);

    /**
     * Delete Transaction
     * 
     * @param id Transaction ID
     */
    void deleteTransaction(String id);

    /**
     * Check if transaction ID exists
     * 
     * @param id Transaction ID
     * @return Exists or Not
     */
    boolean existsById(String id);
} 