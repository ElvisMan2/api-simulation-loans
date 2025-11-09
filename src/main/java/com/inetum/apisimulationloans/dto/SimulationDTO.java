package com.inetum.apisimulationloans.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SimulationDTO {
    private Long id;
    private Double loanAmount;
    private String currency;
    private Double interestRate;
    private Integer term;
    private Double monthlyPayment;
    private Double totalPayment;
    private Boolean approved;
    private LocalDateTime createdAt;
    private Long clientId;
}
