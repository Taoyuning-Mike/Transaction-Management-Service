package com.bank.transaction.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Global Exception Handler
 * 
 * @author YUNING TAO
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Transaction exception
     */
    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<ErrorResponse> handleTransactionException(TransactionException ex) {
        logger.warn("Transaction Exception: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "Transaction Exception",
                ex.getMessage(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * System exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        logger.error("System Exception: ", ex);
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "System Exception",
                "System Internal Exception, please try again later",
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Error response
     */
    public static class ErrorResponse {
        private int status;
        private String error;
        private String message;
        private LocalDateTime timestamp;

        public ErrorResponse(int status, String error, String message, LocalDateTime timestamp) {
            this.status = status;
            this.error = error;
            this.message = message;
            this.timestamp = timestamp;
        }

        public int getStatus() {
            return status;
        }
        public void setStatus(int status) {
            this.status = status;
        }
        public String getError() {
            return error;
        }
        public void setError(String error) {
            this.error = error;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }

    }
} 