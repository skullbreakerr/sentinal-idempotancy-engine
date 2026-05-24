package com.example.sentinal_idempotancy_engine.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarbonResponse {
    private String mcc;
    private String category;
    private double co2GramsPerDollar;
    private String sustainabilityTip;
}
