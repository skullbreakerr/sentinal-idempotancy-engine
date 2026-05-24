package com.example.sentinal_idempotancy_engine.aspect;

import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.example.sentinal_idempotancy_engine.annotation.Idempotent;

import jakarta.servlet.http.HttpServletRequest;

@Aspect
@Component
public class IdempotencyAspect {

    @Autowired
    private RedisTemplate<String,String> redisTemplate;

    @Around("@annotation(idempotent)")
    public Object handleIdempotency(ProceedingJoinPoint joinPoint, Idempotent idempotent) throws Throwable {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        
        // 1. Extract the unique key from Header
        String idempotencyKey = request.getHeader("X-Idempotency-Key");
        if (idempotencyKey == null) {
            throw new RuntimeException("Missing X-Idempotency-Key header");
        }

        String fullKey = idempotent.keyPrefix() + idempotencyKey;

        // 2. Try to set the key in Redis (Atomic SET if Not Exists)
        Boolean isFirstRequest = redisTemplate.opsForValue().setIfAbsent(fullKey, "PROCESSING", Duration.ofSeconds(idempotent.expireIn()));

        if (Boolean.FALSE.equals(isFirstRequest)) {
            throw new RuntimeException("Conflict: Request already in progress or completed.");
        }

        try {
            // 3. Execute the actual business logic (e.g., the Payment)
            Object result = joinPoint.proceed();
            // Optional: Update Redis with the actual response to return it on retries
            return result;
        } catch (Exception e) {
            // 4. If it fails, delete the key so the client can retry
            redisTemplate.delete(fullKey);
            throw e;
        }
    }

}
