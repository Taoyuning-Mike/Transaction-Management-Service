package com.bank.transaction.service.impl;

import com.bank.transaction.config.CacheConfig;
import com.bank.transaction.model.dto.PageableResponse;
import com.bank.transaction.model.dto.TransactionRequest;
import com.bank.transaction.model.dto.TransactionResponse;
import com.bank.transaction.exception.TransactionException;
import com.bank.transaction.model.Transaction;
import com.bank.transaction.dao.TransactionDao;
import com.bank.transaction.service.TransactionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Transaction Service
 * 
 * @author YUNING TAO
 */
@Service
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    // Supported Currencies
    private static final Set<String> CURRENCIES = Set.of(
            "USD", "EUR", "GBP", "JPY", "CNY", "AUD", "CAD"
    );

    // Supported Transaction types
    private static final Set<String> TRANSACTION_TYPES = Set.of(
            "DEPOSIT", "WITHDRAWAL", "TRANSFER"
    );

    private final TransactionDao transactionDao;

    @Autowired
    public TransactionServiceImpl(TransactionDao transactionDao) {
        this.transactionDao = transactionDao;
    }

    @Override
    @CacheEvict(value = CacheConfig.TRANSACTION_LIST_CACHE, allEntries = true)
    public TransactionResponse createTransaction(TransactionRequest request) {
        logger.info("Start creating transaction, Request:{}", request);

        // Validating Request
        validateTransactionRequest(request);

        // Check Duplicated Request
        if (request.getTransactionReference() != null &&
            !request.getTransactionReference().trim().isEmpty() &&
            transactionDao.existsByTransactionReference(request.getTransactionReference())) {
            throw TransactionException.withTransactionReference(request.getTransactionReference());
        }

        // Create Transaction Object
        Transaction transaction = new Transaction(
                request.getAmount(),
                request.getCurrency(),
                request.getTransactionType(),
                request.getTransactionReference()
        );

        // Save Transaction
        try {
            Transaction savedTransaction = transactionDao.save(transaction);
            logger.info("Transaction created successfully, ID:{}", savedTransaction.getId());
            return convertToResponse(savedTransaction);
        } catch (IllegalArgumentException e) {
            throw new TransactionException("Transaction created failed:" + e.getMessage());
        }
    }

    @Override
    @Cacheable(value = CacheConfig.TRANSACTION_ID_CACHE, key = "#id")
    public TransactionResponse getTransactionById(String id) {
        logger.debug("Get Transaction by ID：{}", id);

        if (id == null || id.trim().isEmpty()) {
            throw new TransactionException("Transaction ID cannot be empty.");
        }

        Transaction transaction = transactionDao.findById(id)
                .orElseThrow(() -> TransactionException.withId(id));

        return convertToResponse(transaction);
    }

    @Override
    @Cacheable(value = CacheConfig.TRANSACTION_LIST_CACHE, key = "'page:' + #page + ':size:' + #size")
    public PageableResponse<TransactionResponse> getTransactions(int page, int size) {
        logger.debug("Get Pageable response，page number:{}, page size:{}", page, size);

        if (page < 0) {
            throw new TransactionException("page Number should not be less than 0.");
        }
        if (size <= 0 || size > 100) {
            throw new TransactionException("Page size should be between 1 to 100.");
        }

        List<Transaction> transactions = transactionDao.findAll(page, size);
        long totalElements = transactionDao.count();

        List<TransactionResponse> responseList = transactions.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return new PageableResponse<>(responseList, page, size, totalElements);
    }

    @Override
    @CacheEvict(value = {CacheConfig.TRANSACTION_ID_CACHE, CacheConfig.TRANSACTION_LIST_CACHE}, allEntries = true)
    public TransactionResponse updateTransaction(String id, TransactionRequest request) {
        logger.info("Start Updating Transaction ID:{}, Request:{}", id, request);

        if (id == null || id.trim().isEmpty()) {
            throw new TransactionException("Transaction ID cannot be empty");
        }

        // Validate Request
        validateTransactionRequest(request);

        // Get existing transaction request
        Transaction existingTransaction = transactionDao.findById(id)
                .orElseThrow(() -> TransactionException.withId(id));

        // Update transaction information
        existingTransaction.setAmount(request.getAmount());
        existingTransaction.setCurrency(request.getCurrency());
        existingTransaction.setTransactionType(request.getTransactionType());
        existingTransaction.setTransactionReference(request.getTransactionReference());

        // save Updates
        try {
            Transaction updatedTransaction = transactionDao.save(existingTransaction);
            logger.info("Transaction update ID successfully:{}", updatedTransaction.getId());
            return convertToResponse(updatedTransaction);
        } catch (IllegalArgumentException e) {
            throw new TransactionException("Transaction update ID failed" + e.getMessage());
        }
    }

    @Override
    @CacheEvict(value = {CacheConfig.TRANSACTION_ID_CACHE, CacheConfig.TRANSACTION_LIST_CACHE}, allEntries = true)
    public void deleteTransaction(String id) {
        logger.info("Start deleting transaction by ID：{}", id);

        if (id == null || id.trim().isEmpty()) {
            throw new TransactionException("Transaction ID cannot be empty.");
        }

        if (!transactionDao.existsById(id)) {
            throw TransactionException.withId(id);
        }

        boolean deleted = transactionDao.deleteById(id);
        if (deleted) {
            logger.info("Transaction delete successfully for ID: {}", id);
        } else {
            throw new RuntimeException("Transaction delete failed for ID:" + id);
        }
    }

    @Override
    public boolean existsById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return transactionDao.existsById(id);
    }

    /**
     * Validating Request
     */
    private void validateTransactionRequest(TransactionRequest request) {
        if (request == null) {
            throw new TransactionException("Transaction request cannot be empty.");
        }

        if (request.getAmount()  <= 0.0) {
            throw TransactionException.invalidAmount();
        }

        if (request.getCurrency() == null || 
            request.getCurrency().trim().isEmpty() ||
            !CURRENCIES.contains(request.getCurrency().toUpperCase())) {
            throw TransactionException.invalidCurrency(request.getCurrency());
        }

        if (request.getTransactionType() == null || 
            request.getTransactionType().trim().isEmpty() ||
            !TRANSACTION_TYPES.contains(request.getTransactionType().toUpperCase())) {
            throw TransactionException.invalidTransactionType(request.getTransactionType());
        }
    }

    /**
     * Convert to Transaction response
     */
    private TransactionResponse convertToResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getCurrency(),
                transaction.getTransactionType(),
                transaction.getTransactionReference(),
                transaction.getTimestamp()
        );
    }
} 