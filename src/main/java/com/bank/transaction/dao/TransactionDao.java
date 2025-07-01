package com.bank.transaction.dao;

import com.bank.transaction.model.Transaction;

import java.util.List;
import java.util.Optional;

/**
 * Transaction DAO Interface
 * 
 * @author YUNING TAO
 */
public interface TransactionDao {

    /**
     * save transaction
     * 
     * @param transaction transaction object
     * @return saved transaction
     */
    Transaction save(Transaction transaction);

    /**
     * find transaction by ID
     * 
     * @param id transaction id
     * @return transaction Object
     */
    Optional<Transaction> findById(String id);

    /**
     * find all transaction
     * 
     * @return transaction list
     */
    List<Transaction> findAll();

    /**
     * Pageable transaction results
     * 
     * @param page page number
     * @param size page size
     * @return transaction list
     */
    List<Transaction> findAll(int page, int size);

    /**
     * Get transaction count
     * 
     * @return transaction count
     */
    long count();

    /**
     * Delete By ID
     * 
     * @param id TransactionID
     * @return Success or Not
     */
    boolean deleteById(String id);

    /**
     * If Transaction exists
     * 
     * @param id transaction id
     * @return Exists or Not
     */
    boolean existsById(String id);

    /**
     * If Transaction exists
     * 
     * @param transactionReference transaction Reference
     * @return Exists or Not
     */
    boolean existsByTransactionReference(String transactionReference);
} 