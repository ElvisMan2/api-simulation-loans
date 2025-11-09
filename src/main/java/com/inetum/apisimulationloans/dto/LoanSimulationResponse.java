package com.inetum.apisimulationloans.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoanSimulationResponse {
    private boolean approved;
    private double monthlyPayment;
    private double totalPayment;
    private String message;
}
