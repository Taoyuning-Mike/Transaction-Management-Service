package com.bank.transaction.exception;

public class TransactionException  extends RuntimeException{
    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

    public static TransactionException withTransactionReference(String transactionReference) {
        return new TransactionException("Duplicated Transaction Reference:" + transactionReference);
    }

    public static TransactionException invalidAmount() {
        return new TransactionException("Invalid Transaction Amount，Must be over 0");
    }

    public static TransactionException invalidCurrency(String currency) {
        return new TransactionException("Invalid Currency: " + currency);
    }

    public static TransactionException invalidTransactionType(String type) {
        return new TransactionException("Invalid Transaction Type: " + type);
    }

    public static TransactionException withId(String id) {
        return new TransactionException("Not Found Transaction ID: " + id);
    }

}
