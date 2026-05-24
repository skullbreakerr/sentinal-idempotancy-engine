package com.example.sentinal_idempotancy_engine.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String transactionId; // The unique ID from the user
    private Double amount;
    private String currency;
    private String mcc;          // Merchant Category Code (e.g., 5541)

    // --- GreenPay Specific Fields ---
    private Double co2Grams;     // Calculated CO2
    private String sustainabilityCategory; // e.g., "High Carbon - Fuel"

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}
