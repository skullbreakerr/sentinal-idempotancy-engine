package com.example.sentinal_idempotancy_engine.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

/**
 * Marker annotation for Sentinel Idempotency Engine.
 * Apply this to Controller methods to prevent double-processing.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Idempotent {

    /**
     * Prefix for the Redis key to avoid collisions between different APIs.
     * Example: "payment-service:"
     */
    String keyPrefix() default "sentinel:";

    /**
     * The duration for which the idempotency key remains valid.
     */
    long expireIn() default 60;

    /**
     * The time unit for the expiration duration.
     */
    TimeUnit unit() default TimeUnit.MINUTES;

    /**
     * The name of the HTTP Header that contains the unique Request ID.
     * Default follows standard financial API naming conventions.
     */
    String headerName() default "X-Idempotency-Key";

    /**
     * Message to return when a duplicate request is detected.
     */
    String message() default "Conflict: A request with this ID is already being processed or has completed.";
}