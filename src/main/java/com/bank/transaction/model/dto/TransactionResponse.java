package com.bank.transaction.model.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Transaction response
 * 
 * @author YUNING TAO
 */
public class TransactionResponse {

    private String id;
    private double amount;
    private String currency;
    private String transactionType;
    private LocalDateTime timestamp;
    private String transactionReference;

    public TransactionResponse() {}

    public TransactionResponse(String id, double amount, String currency, String transactionType,
                             LocalDateTime timestamp, String transactionReference) {
        this.id = id;
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
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

} 