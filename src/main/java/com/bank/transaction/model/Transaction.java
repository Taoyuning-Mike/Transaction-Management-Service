package com.bank.transaction.model;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * Transaction model
 * 
 * @author YUNING TAO
 */
public class Transaction {

    private String id;

    @NotNull
    private double amount;

    @NotBlank
    private String currency;

    @NotBlank
    private String transactionType;

    private LocalDateTime timestamp;

    private String transactionReference;

    public Transaction() {
        this.id = UUID.randomUUID().toString();
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(double amount, String currency, String transactionType, String transactionReference) {
        this();
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
        this.transactionReference = transactionReference;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

} 