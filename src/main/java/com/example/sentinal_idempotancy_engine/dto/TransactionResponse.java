package com.example.sentinal_idempotancy_engine.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponse {

    private String transactionId;
    private String status; // e.g., "SUCCESS", "FILTERED_BY_SHIELDGATE"
    
    // --- GreenPay Data ---
    private Double carbonFootprintGrams;
    private String sustainabilityGrade; // e.g., "Eco-Friendly", "High Carbon"
    private String suggestion;          // e.g., "Consider public transport next time!"

    private LocalDateTime processedAt;
    private String message;
}