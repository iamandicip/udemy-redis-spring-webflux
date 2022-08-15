package com.ic.redis.spring.fib.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class FibService {

    // we need a strategy for cache eviction
    @Cacheable(value = "math:fib", key = "#index")
    public Long getFib(long index) {
        System.out.println("Calculating fibonacci for " + index);
        return fib(index);
    }

    // on PUT, POST, PATCH, DELETE
    @CacheEvict(value = "math:fib", key = "#index")
    public void clearCache(long index) {
        System.out.println("Clearing cache for " + index);
    }

    @Scheduled(fixedRate = 100_000)
    @CacheEvict(value = "math:fib", allEntries = true)
    public void clearCache() {
        System.out.println("Clearing all cache for math:fib");
    }

    private Long fib(long index) {
        if (index < 2) {
            return index;
        }
        return fib(index - 1) + fib(index - 2);
    }
}
