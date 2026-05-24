package com.example.sentinal_idempotancy_engine.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {

    @NotBlank(message = "X-Request-ID header is missing or empty")
    private String xRequestId; // Used by Sentinel for Idempotency

    @NotNull(message = "Transaction amount is required")
    @Min(value = 1, message = "Amount must be greater than zero")
    private Double amount;

    @NotBlank(message = "Currency code is required (e.g., USD, INR)")
    private String currency;

    @NotBlank(message = "MCC is required for GreenPay calculation")
    private String mcc; // Merchant Category Code (e.g., 5541 for Gas)

    private String merchantName;
}