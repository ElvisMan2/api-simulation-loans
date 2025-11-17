package com.inetum.apisimulationloans.service;

import com.inetum.apisimulationloans.dto.SimulationResponse;
import com.inetum.apisimulationloans.dto.SimulationRequest;
import com.inetum.apisimulationloans.mapper.ClientMapper;
import com.inetum.apisimulationloans.mapper.SimulationMapper;
import com.inetum.apisimulationloans.model.Client;
import com.inetum.apisimulationloans.model.Simulation;
import com.inetum.apisimulationloans.repository.ClientRepository;
import com.inetum.apisimulationloans.repository.SimulationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SimulationServiceTest {

    @InjectMocks
    private SimulationService simulationService;

    @Mock
    private SimulationRepository simulationRepository;

    @Mock
    private ClientRepository clientRepository;

    // NO MOCKEADO:
    private SimulationMapper simulationMapper;
    private ClientMapper clientMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        simulationMapper = Mappers.getMapper(SimulationMapper.class);
        clientMapper = Mappers.getMapper(ClientMapper.class);
        simulationService = new SimulationService(
                simulationRepository,
                clientRepository,
                simulationMapper,
                clientMapper
        );
    }

    @Test
    void testGetSimulationsByClientId_success() {
        // Arrange
        Long clientId = 1L;
        Client client = new Client();
        client.setClientId(clientId);

        Simulation simulation1 = new Simulation();
        simulation1.setSimulationId(100L);
        simulation1.setLoanAmount(5000.0);
        simulation1.setCurrency("USD");
        simulation1.setInterestRate(10.0);
        simulation1.setTerm(12);
        simulation1.setInstallment(430.0);
        simulation1.setTotalPayment(5160.0);
        simulation1.setAcceptance(true);
        simulation1.setSimulationDate(LocalDateTime.now());
        simulation1.setDisbursementDate(LocalDate.now());

        Simulation simulation2 = new Simulation();
        simulation2.setSimulationId(101L);
        simulation2.setLoanAmount(8000.0);
        simulation2.setCurrency("USD");
        simulation2.setInterestRate(12.0);
        simulation2.setTerm(24);
        simulation2.setInstallment(377.0);
        simulation2.setTotalPayment(9048.0);
        simulation2.setAcceptance(false);
        simulation2.setSimulationDate(LocalDateTime.now());
        simulation2.setDisbursementDate(LocalDate.now());

        client.setSimulations(List.of(simulation1, simulation2));

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Act
        List<SimulationResponse> result = simulationService.getSimulationsByClientId(clientId);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        SimulationResponse r1 = result.get(0);
        assertEquals(simulation1.getSimulationId(), r1.getSimulationId());
        assertEquals(simulation1.getLoanAmount(), r1.getLoanAmount());
        assertEquals(simulation1.getCurrency(), r1.getCurrency());

        SimulationResponse r2 = result.get(1);
        assertEquals(simulation2.getSimulationId(), r2.getSimulationId());

        verify(clientRepository).findById(clientId);
    }

    @Test
    void testGetSimulationsByClientId_whenClientDoesNotExist() {
        // Arrange
        Long clientId = 999L;
        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act & Assert
        NoSuchElementException exception = assertThrows(
                NoSuchElementException.class,
                () -> simulationService.getSimulationsByClientId(clientId)
        );

        assertEquals("Client not found with ID: 999", exception.getMessage());
        verify(clientRepository).findById(clientId);
    }

    @Test
    void testSimulateAndSave_savesAndReturnsMappedResponse() {
        // Arrange
        Long clientId = 1L;
        Client client = new Client();
        client.setClientId(clientId);
        client.setMonthlyIncome(2000.0);
        // no simulations preexist
        client.setSimulations(List.of());

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        SimulationRequest request = SimulationRequest.builder()
                .loanAmount(5000.0)
                .currency("USD")
                .interestRate(10.0) // anual
                .term(12) // meses
                .disbursementDate(LocalDate.of(2025, 12, 1))
                .build();

        // Mock save to set an id and return the saved entity
        when(simulationRepository.save(any(Simulation.class))).thenAnswer(invocation -> {
            Simulation s = invocation.getArgument(0);
            s.setSimulationId(42L);
            // Ensure simulationDate is set by service; leave other fields as set
            return s;
        });

        // Act
        SimulationResponse response = simulationService.createSimulation(clientId, request);

        // Assert basic mapping and persistence interactions
        assertNotNull(response);
        assertEquals(42L, response.getSimulationId());
        assertEquals(request.getLoanAmount(), response.getLoanAmount());
        assertEquals(request.getCurrency(), response.getCurrency());
        assertEquals(request.getInterestRate(), response.getInterestRate());
        assertEquals(request.getTerm(), response.getTerm());
        assertEquals(request.getDisbursementDate(), response.getDisbursementDate());
        assertEquals(clientId, response.getClientId());

        // Recalculate expected monthly payment (mismo algoritmo que el servicio)
        double loanAmount = request.getLoanAmount();
        double annualRate = request.getInterestRate();
        int term = request.getTerm();
        double monthlyRate = annualRate / 12 / 100;
        double numerator = monthlyRate * Math.pow(1 + monthlyRate, term);
        double denominator = Math.pow(1 + monthlyRate, term) - 1;
        double rawMonthlyPayment = loanAmount * (numerator / denominator);
        double rawTotalPayment = rawMonthlyPayment * term;

        double expectedMonthly = BigDecimal.valueOf(rawMonthlyPayment)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
        double expectedTotal = BigDecimal.valueOf(rawTotalPayment)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();

        assertEquals(expectedMonthly, response.getMonthlyPayment());
        assertEquals(expectedTotal, response.getTotalPayment());

        // approved: monthlyPayment <= 50% monthlyIncome (2000 * 0.5 = 1000)
        assertTrue(response.getApproved());

        // createdAt should be set by service (no es null)
        assertNotNull(response.getCreatedAt());

        verify(clientRepository).findById(clientId);
        verify(simulationRepository).save(any(Simulation.class));
    }
}
