package com.inetum.apisimulationloans.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/simulations")
public class SimulationController {

    @GetMapping("/hardcoded")
    public ResponseEntity<Map<String, Object>> getHardcodedSimulation() {

        // 🔹 Subobjeto "customer"
        Map<String, Object> customer = new LinkedHashMap<>();
        customer.put("customerId", 5);
        customer.put("firstName", "Lucas");
        customer.put("lastNameFather", "Anderson");
        customer.put("lastNameMother", "Reed");
        customer.put("monthlyIncome", 5500);

        // 🔹 Subobjeto "simulation"
        Map<String, Object> simulation = new LinkedHashMap<>();
        simulation.put("simulationId", 3);
        simulation.put("customerId", 5);
        simulation.put("loanAmount", 12000);
        simulation.put("interestRate", 6.5);
        simulation.put("termMonths", 36);
        simulation.put("installment", 366);
        simulation.put("accepted", false);

        // Fecha con formato personalizado
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = LocalDateTime.of(2024, 1, 15, 18, 30, 45).format(formatter);
        simulation.put("simulationDate", formattedDate);
        simulation.put("customer", customer);

        // 🔹 Objeto principal de respuesta
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("message", "Simulation created successfully");
        response.put("simulation", simulation);

        return ResponseEntity.ok(response);
    }
}
