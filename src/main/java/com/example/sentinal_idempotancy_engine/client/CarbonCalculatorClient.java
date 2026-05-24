package com.example.sentinal_idempotancy_engine.client;

import com.example.sentinal_idempotancy_engine.dto.CarbonResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

// In interview, say: "I used Feign to decouple the carbon logic from the payment logic."
@FeignClient(name = "carbon-calculator-service", url = "${external.carbon-api.url}")
public interface CarbonCalculatorClient {

    @GetMapping("/v1/impact/{mcc}")
    CarbonResponse getCarbonImpact(@PathVariable("mcc") String mcc);
}