package com.bank.transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * Bank Transaction Management Application
 * 
 * @author YUNING TAO
 */
@SpringBootApplication
@EnableCaching
public class TransactionManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(TransactionManagementApplication.class, args);
    }
} 