package com.inetum.apisimulationloans.controller;

import com.inetum.apisimulationloans.dto.SimulationRequest;
import com.inetum.apisimulationloans.dto.SimulationResponse;
import com.inetum.apisimulationloans.service.ClientService;
import com.inetum.apisimulationloans.service.SimulationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/simulations")
public class SimulationController {

    //Endpoints reales

    //inyeccion de dependencias:
    private final ClientService clientService;
    private final SimulationService simulationService;

    public SimulationController(ClientService clientService, SimulationService simulationService) {
        this.clientService = clientService;
        this.simulationService = simulationService;
    }

    @PostMapping("/client/{clientId}")
    public List<Object> simulateLoanAndSave(
            @PathVariable Long clientId,
            @Valid @RequestBody SimulationRequest request) {

        if(request.getDisbursementDate().isBefore(LocalDate.now())){
            throw new IllegalArgumentException("Disbursement date cannot be before today");
        }

        SimulationResponse sim = simulationService.createSimulation(clientId, request);

        List<Object> response = new ArrayList<>();

        if(sim.getApproved()){
            response.add("Loan simulation approved");
        }
        else {
            response.add("Loan simulation not approved");
        }

        response.add(sim);

        return response;
    }

    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<SimulationResponse>> getSimulationsByClientId(@PathVariable Long clientId) {
        List<SimulationResponse> simulations = simulationService.getSimulationsByClientId(clientId);
        return ResponseEntity.ok(simulations);
    }

}


