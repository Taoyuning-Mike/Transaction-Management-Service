package com.bank.transaction.model.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/**
 * Transaction Request
 * 
 * @author YUNING TAO
 */
public class TransactionRequest {
    private double amount;

    private String currency;

    private String transactionType;

    private String transactionReference;

    public TransactionRequest() {}

    public TransactionRequest(double amount, String currency, String transactionType, String transactionReference) {
        this.amount = amount;
        this.currency = currency;
        this.transactionType = transactionType;
        this.transactionReference = transactionReference;
    }

    // Getters and Setters
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

    public String getTransactionReference() {
        return transactionReference;
    }

    public void setTransactionReference(String transactionReference) {
        this.transactionReference = transactionReference;
    }

    @Override
    public String toString() {
        return "TransactionRequest{" +
                "amount=" + amount +
                ", currency='" + currency + '\'' +
                ", transactionType='" + transactionType + '\'' +
                ", transactionReference='" + transactionReference + '\'' +
                '}';
    }
} 