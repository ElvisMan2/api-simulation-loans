package com.inetum.apisimulationloans.controller;

import com.inetum.apisimulationloans.dto.LoanSimulationRequest;
import com.inetum.apisimulationloans.dto.LoanSimulationResponse;
import com.inetum.apisimulationloans.dto.SimulationDTO;
import com.inetum.apisimulationloans.model.Client;
import com.inetum.apisimulationloans.service.ClientService;
import com.inetum.apisimulationloans.service.LoanSimulationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/simulations")
public class SimulationController {

    //Endpoints hardcodeados:
    @PostMapping("/create")//harcoded
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

    @GetMapping("/customer/{customerId}")//hardcoded
    public ResponseEntity<List<Map<String, Object>>> getSimulationsByCustomerId(@PathVariable Integer customerId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        List<Map<String, Object>> simulations = new ArrayList<>();

        // Simulación 1
        Map<String, Object> s1 = new LinkedHashMap<>();
        s1.put("simulationId", 1);
        s1.put("customerId", customerId);
        s1.put("loanAmount", 10000);
        s1.put("interestRate", 5.5);
        s1.put("termMonths", 24);
        s1.put("installment", 440);
        s1.put("accepted", true);
        s1.put("simulationDate", LocalDateTime.of(2024, 1, 15, 18, 30, 45).format(formatter));

        // Simulación 2
        Map<String, Object> s2 = new LinkedHashMap<>();
        s2.put("simulationId", 2);
        s2.put("customerId", customerId);
        s2.put("loanAmount", 20000);
        s2.put("interestRate", 5.5);
        s2.put("termMonths", 24);
        s2.put("installment", 440);
        s2.put("accepted", true);
        s2.put("simulationDate", LocalDateTime.of(2024, 1, 16, 18, 30, 45).format(formatter));

        // Simulación 3
        Map<String, Object> s3 = new LinkedHashMap<>();
        s3.put("simulationId", 3);
        s3.put("customerId", customerId);
        s3.put("loanAmount", 30000);
        s3.put("interestRate", 5.5);
        s3.put("termMonths", 24);
        s3.put("installment", 440);
        s3.put("accepted", true);
        s3.put("simulationDate", LocalDateTime.of(2024, 1, 17, 18, 30, 45).format(formatter));

        simulations.add(s1);
        simulations.add(s2);
        simulations.add(s3);

        return ResponseEntity.ok(simulations);
    }

    @GetMapping("/customer/{customerId}/latest")//hardcoded
    public ResponseEntity<Map<String, Object>> getLatestSimulationByCustomerId(@PathVariable Integer customerId) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        Map<String, Object> latestSimulation = new LinkedHashMap<>();
        latestSimulation.put("simulationId", 3);
        latestSimulation.put("customerId", customerId);
        latestSimulation.put("loanAmount", 30000);
        latestSimulation.put("interestRate", 5.5);
        latestSimulation.put("termMonths", 24);
        latestSimulation.put("installment", 440);
        latestSimulation.put("accepted", true);
        latestSimulation.put("simulationDate", LocalDateTime.of(2024, 1, 17, 18, 30, 45).format(formatter));

        return ResponseEntity.ok(latestSimulation);
    }

    //Endpoints reales

    //inyeccion de dependencias:
    private final ClientService clientService;
    private final LoanSimulationService loanSimulationService;

    public SimulationController(ClientService clientService, LoanSimulationService loanSimulationService) {
        this.clientService = clientService;
        this.loanSimulationService = loanSimulationService;
    }

    @PostMapping("/client/{clientId}")
    public ResponseEntity<SimulationDTO> simulateLoanAndSave(
            @PathVariable Long clientId,
            @Valid @RequestBody LoanSimulationRequest request) {

        Client client = clientService.getClientByIdOrThrow(clientId);
        SimulationDTO response = loanSimulationService.simulateAndSave(client, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<SimulationDTO>> getSimulationsByClientId(@PathVariable Long clientId) {
        List<SimulationDTO> simulations = loanSimulationService.getSimulationsByClientId(clientId);
        return ResponseEntity.ok(simulations);
    }

}
