package com.bank.transaction.dao.impl;

import com.bank.transaction.model.Transaction;
import com.bank.transaction.dao.TransactionDao;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Transaction DAO
 * 
 * @author YUNING TAO
 */
@Repository
public class TransactionDaoImpl implements TransactionDao {

    private final Map<String, Transaction> transactions = new ConcurrentHashMap<>();
    private final Map<String, String> transactionReferenceToId = new ConcurrentHashMap<>();

    @Override
    public Transaction save(Transaction transaction) {
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be empty");
        }

        // Check if transaction reference already exists.
        if (transaction.getTransactionReference() != null &&
            !transaction.getTransactionReference().trim().isEmpty()) {
            String existingId = transactionReferenceToId.get(transaction.getTransactionReference());
            if (existingId != null && !existingId.equals(transaction.getId())) {
                throw new IllegalArgumentException("Transaction reference already exists: " + transaction.getTransactionReference());
            }
        }

        // Save transactions
        transactions.put(transaction.getId(), transaction);
        
        // Update Transaction reference & ID mapping
        if (transaction.getTransactionReference() != null &&
            !transaction.getTransactionReference().trim().isEmpty()) {
            transactionReferenceToId.put(transaction.getTransactionReference(), transaction.getId());
        }

        return transaction;
    }

    @Override
    public Optional<Transaction> findById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return Optional.empty();
        }
        return Optional.ofNullable(transactions.get(id));
    }

    @Override
    public List<Transaction> findAll() {
        return new ArrayList<>(transactions.values())
                .stream()
                //sort by timestamp desc
                .sorted((t1, t2) -> t2.getTimestamp().compareTo(t1.getTimestamp()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> findAll(int page, int size) {
        if (page < 0 || size <= 0) {
            return Collections.emptyList();
        }

        List<Transaction> transactions = findAll();
        int start = page * size;
        int end = Math.min(start + size, transactions.size());

        if (start >= transactions.size()) {
            return Collections.emptyList();
        }

        return transactions.subList(start, end);
    }

    @Override
    public long count() {
        return transactions.size();
    }

    public Optional<Transaction> findByTransactionReference(String transactionReference) {
        if (transactionReference == null || transactionReference.trim().isEmpty()) {
            return Optional.empty();
        }
        
        String id = transactionReferenceToId.get(transactionReference);
        if (id != null) {
            return findById(id);
        }
        return Optional.empty();
    }

    @Override
    public boolean deleteById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }

        Transaction transaction = transactions.remove(id);
        if (transaction != null) {
            if (transaction.getTransactionReference() != null) {
                transactionReferenceToId.remove(transaction.getTransactionReference());
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean existsById(String id) {
        if (id == null || id.trim().isEmpty()) {
            return false;
        }
        return transactions.containsKey(id);
    }

    @Override
    public boolean existsByTransactionReference(String transactionReference) {
        if (transactionReference == null || transactionReference.trim().isEmpty()) {
            return false;
        }
        return transactionReferenceToId.containsKey(transactionReference);
    }

    public int size() {
        return transactions.size();
    }
} 