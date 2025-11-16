package com.inetum.apisimulationloans.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inetum.apisimulationloans.dto.SimulationRequest;
import com.inetum.apisimulationloans.dto.SimulationResponse;
import com.inetum.apisimulationloans.exception.ClientNotFoundException;
import com.inetum.apisimulationloans.service.ClientService;
import com.inetum.apisimulationloans.service.SimulationService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SimulationController.class)
class SimulationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimulationService simulationService;

    @MockBean
    private ClientService clientService; // inyectado en el controller aunque no se use en todos los tests

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSimulateLoanAndSave_approved() throws Exception {
        Long clientId = 1L;
        LocalDate disbDate = LocalDate.now().plusDays(1);
        SimulationRequest req = SimulationRequest.builder()
                .loanAmount(10000.0)
                .currency("USD")
                .interestRate(5.0)
                .term(12)
                .disbursementDate(disbDate)
                .build();

        SimulationResponse resp = SimulationResponse.builder()
                .simulationId(100L)
                .loanAmount(10000.0)
                .currency("USD")
                .interestRate(5.0)
                .term(12)
                .monthlyPayment(856.0)
                .totalPayment(10272.0)
                .approved(true)
                .createdAt(LocalDateTime.now())
                .disbursementDate(disbDate)
                .clientId(clientId)
                .build();

        when(simulationService.createSimulation(Mockito.eq(clientId), any(SimulationRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/simulations/client/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Loan simulation approved"))
                .andExpect(jsonPath("$[1].simulationId").value(100))
                .andExpect(jsonPath("$[1].approved").value(true));
    }

    @Test
    void testSimulateLoanAndSave_notApproved() throws Exception {
        Long clientId = 2L;
        LocalDate disbDate = LocalDate.now().plusDays(2);
        SimulationRequest req = SimulationRequest.builder()
                .loanAmount(50000.0)
                .currency("USD")
                .interestRate(10.0)
                .term(24)
                .disbursementDate(disbDate)
                .build();

        SimulationResponse resp = SimulationResponse.builder()
                .simulationId(101L)
                .loanAmount(50000.0)
                .currency("USD")
                .interestRate(10.0)
                .term(24)
                .monthlyPayment(2300.0)
                .totalPayment(55200.0)
                .approved(false)
                .createdAt(LocalDateTime.now())
                .disbursementDate(disbDate)
                .clientId(clientId)
                .build();

        when(simulationService.createSimulation(Mockito.eq(clientId), any(SimulationRequest.class))).thenReturn(resp);

        mockMvc.perform(post("/simulations/client/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Loan simulation not approved"))
                .andExpect(jsonPath("$[1].approved").value(false));
    }

    @Test
    void testSimulateLoanAndSave_disbursementDateBeforeToday_throwsBadRequest() throws Exception {
        Long clientId = 3L;
        LocalDate pastDate = LocalDate.now().minusDays(1);
        SimulationRequest req = SimulationRequest.builder()
                .loanAmount(2000.0)
                .currency("USD")
                .interestRate(4.0)
                .term(6)
                .disbursementDate(pastDate)
                .build();

        mockMvc.perform(post("/simulations/client/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Disbursement date cannot be before today"));

        // servicio no debe ser invocado
        verify(simulationService, Mockito.never()).createSimulation(any(Long.class), any(SimulationRequest.class));
    }

    @Test
    void testSimulateLoanAndSave_serviceThrowsRuntimeException_returnsInternalServerError() throws Exception {
        Long clientId = 4L;
        LocalDate disbDate = LocalDate.now().plusDays(3);
        SimulationRequest req = SimulationRequest.builder()
                .loanAmount(3000.0)
                .currency("EUR")
                .interestRate(3.0)
                .term(12)
                .disbursementDate(disbDate)
                .build();

        when(simulationService.createSimulation(Mockito.eq(clientId), any(SimulationRequest.class)))
                .thenThrow(new RuntimeException("Simulation failed unexpectedly"));

        mockMvc.perform(post("/simulations/client/{clientId}", clientId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Simulation failed unexpectedly"));
    }

    @Test
    void testGetSimulationsByClientId_success() throws Exception {
        Long clientId = 5L;
        LocalDate disbDate = LocalDate.now().plusDays(5);
        SimulationResponse r1 = SimulationResponse.builder()
                .simulationId(201L)
                .loanAmount(1500.0)
                .currency("USD")
                .interestRate(2.5)
                .term(6)
                .monthlyPayment(255.0)
                .totalPayment(1530.0)
                .approved(true)
                .createdAt(LocalDateTime.now())
                .disbursementDate(disbDate)
                .clientId(clientId)
                .build();

        SimulationResponse r2 = SimulationResponse.builder()
                .simulationId(202L)
                .loanAmount(2500.0)
                .currency("USD")
                .interestRate(3.0)
                .term(12)
                .monthlyPayment(214.0)
                .totalPayment(2568.0)
                .approved(false)
                .createdAt(LocalDateTime.now())
                .disbursementDate(disbDate)
                .clientId(clientId)
                .build();

        List<SimulationResponse> list = Arrays.asList(r1, r2);
        when(simulationService.getSimulationsByClientId(clientId)).thenReturn(list);

        mockMvc.perform(get("/simulations/client/{clientId}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].simulationId").value(201))
                .andExpect(jsonPath("$[1].simulationId").value(202));
    }

    @Test
    void testGetSimulationsByClientId_whenClientNotFound_returnsNotFound() throws Exception {
        Long clientId = 99L;
        when(simulationService.getSimulationsByClientId(clientId)).thenThrow(new ClientNotFoundException(clientId));

        mockMvc.perform(get("/simulations/client/{clientId}", clientId))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").exists());
    }
}
