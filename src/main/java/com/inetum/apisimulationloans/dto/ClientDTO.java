package com.inetum.apisimulationloans.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class ClientDTO {
    @NotBlank
    private String firstName;

    @NotBlank
    private String paternalLastName;

    @NotBlank
    private String maternalLastName;

    @NotBlank
    private String currencyOfIncome;

    @NotNull
    @Positive
    private Double monthlyIncome;
}

