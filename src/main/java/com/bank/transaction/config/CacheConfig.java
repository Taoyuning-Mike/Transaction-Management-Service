package com.bank.transaction.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

/**
 * Cache Manager
 *
 * @author YUNING TAO
 */
@Configuration
public class CacheConfig {

    public static final String TRANSACTION_ID_CACHE = "transactions";
    public static final String TRANSACTION_LIST_CACHE = "transactionList";

    /**
     * Caffeine Cache configuration
     */
    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager();
        cacheManager.setCaffeine(Caffeine.newBuilder()
                .maximumSize(500)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats());
        return cacheManager;
    }

} 