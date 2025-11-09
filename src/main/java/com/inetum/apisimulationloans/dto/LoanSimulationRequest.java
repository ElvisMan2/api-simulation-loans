package com.inetum.apisimulationloans.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class LoanSimulationRequest {
    @NotNull
    @Positive
    private Double loanAmount;

    @NotBlank
    private String currency;

    @NotNull
    @Positive
    private Double interestRate; // anual

    @NotNull
    @Min(1)
    private Integer term; // meses
}
