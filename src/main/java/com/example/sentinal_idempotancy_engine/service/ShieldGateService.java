package com.example.sentinal_idempotancy_engine.service;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ShieldGateService {

    // A thread-safe map to store buckets for different users
    private final Map<String, Bucket> cache = new ConcurrentHashMap<>();

    private Bucket createNewBucket() {
        // Limit: 5 requests per minute per unique user/IP
        return Bucket.builder()
                .addLimit(Bandwidth.classic(5, Refill.intervally(5, Duration.ofMinutes(1))))
                .build();
    }

    public boolean allowRequest(String key) {
        // Get the bucket for this specific user, or create a new one if it's their first request
        return cache.computeIfAbsent(key, k -> createNewBucket()).tryConsume(1);
    }
}
