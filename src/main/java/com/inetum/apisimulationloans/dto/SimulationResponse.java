package com.inetum.apisimulationloans.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SimulationResponse {
    private Long simulationId;

    // Monto del préstamo debe ser positivo y no nulo
    @NotNull
    @Positive
    private Double loanAmount;

    // Moneda: código de 3 letras mayúsculas, no vacío
    @NotBlank
    @Size(min = 3, max = 3)
    @Pattern(regexp = "^[A-Z]{3}$")
    private String currency;

    // Tasa de interés debe ser > 0
    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private Double interestRate;

    // Plazo mínimo 1
    @NotNull
    @Min(1)
    private Integer term;

    // Pagos y totales deben ser positivos
    @NotNull
    @Positive
    private Double monthlyPayment;

    @NotNull
    @Positive
    private Double totalPayment;

    @NotNull
    private Boolean approved;

    // Fecha de creación no puede ser futura
    @PastOrPresent
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy")
    // Fecha de desembolso no puede ser pasada (o puede ajustarse según requisito)
    @FutureOrPresent
    private LocalDate disbursementDate;

    @NotNull
    private Long clientId;
}
