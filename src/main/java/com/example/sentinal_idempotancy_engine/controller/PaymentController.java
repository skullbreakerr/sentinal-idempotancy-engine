package com.example.sentinal_idempotancy_engine.controller;

import com.example.sentinal_idempotancy_engine.annotation.Idempotent;
import com.example.sentinal_idempotancy_engine.dto.TransactionRequest;
import com.example.sentinal_idempotancy_engine.dto.TransactionResponse;
import com.example.sentinal_idempotancy_engine.service.GreenPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    @Autowired
    private GreenPayService greenPayService;

    @PostMapping("/execute")
    @Idempotent(expireIn = 120, unit = TimeUnit.SECONDS) // [SENTINEL] Logic kicks in automatically
    public ResponseEntity<TransactionResponse> processPayment(@RequestBody TransactionRequest request) {
        
        // 1. If we reached here, ShieldGate (Rate Limiter) has already allowed the IP.
        // 2. Sentinel has already verified this is not a duplicate X-Request-ID.

        // 3. Simulate Core Payment Logic
        String internalTxId = "MTID-" + UUID.randomUUID().toString().substring(0, 8);

        // 4. Trigger GreenPay for Sustainability Calculation
        // In a real system, you might do this via an Event/Message Queue
        greenPayService.calculateAndSaveImpact(request, internalTxId);

        // 5. Build the "Mastercard-Standard" Response
        TransactionResponse response = TransactionResponse.builder()
                .transactionId(internalTxId)
                .status("SUCCESS")
                .message("Payment processed and carbon footprint logged.")
                .processedAt(LocalDateTime.now())
                .build();

        return ResponseEntity.ok(response);
    }
}
