package com.inetum.apisimulationloans.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class SimulationTest {

    @Test
    void testSimulationFullCoverage() {
        // Arrange
        Client client = new Client();
        client.setClientId(10L);

        Loan loan = new Loan();
        loan.setLoanId(50L);

        Simulation simulation = new Simulation();

        LocalDateTime now = LocalDateTime.now();
        LocalDate disbursementDate = LocalDate.of(2025, 12, 1);

        // Act
        simulation.setSimulationId(1L);
        simulation.setLoanAmount(10000.0);
        simulation.setCurrency("USD");
        simulation.setInterestRate(5.5);
        simulation.setTerm(12);
        simulation.setInstallment(860.66);
        simulation.setTotalPayment(10327.92);
        simulation.setAcceptance(true);
        simulation.setSimulationDate(now);
        simulation.setDisbursementDate(disbursementDate);
        simulation.setClient(client);
        simulation.setLoan(loan);

        // Assert
        assertEquals(1L, simulation.getSimulationId());
        assertEquals(10000.0, simulation.getLoanAmount());
        assertEquals("USD", simulation.getCurrency());
        assertEquals(5.5, simulation.getInterestRate());
        assertEquals(12, simulation.getTerm());
        assertEquals(860.66, simulation.getInstallment());
        assertEquals(10327.92, simulation.getTotalPayment());
        assertTrue(simulation.getAcceptance());
        assertEquals(now, simulation.getSimulationDate());
        assertEquals(disbursementDate, simulation.getDisbursementDate());
        assertEquals(client, simulation.getClient());
        assertEquals(loan, simulation.getLoan());
    }

    @Test
    void testSimulationFalseAcceptance() {
        Simulation simulation = new Simulation();
        simulation.setAcceptance(false);
        assertFalse(simulation.getAcceptance());
    }

    @Test
    void testSimulationNullValues() {
        Simulation simulation = new Simulation();

        simulation.setSimulationId(null);
        simulation.setLoanAmount(null);
        simulation.setCurrency(null);
        simulation.setInterestRate(null);
        simulation.setTerm(null);
        simulation.setInstallment(null);
        simulation.setTotalPayment(null);
        simulation.setAcceptance(null);
        simulation.setSimulationDate(null);
        simulation.setDisbursementDate(null);
        simulation.setClient(null);
        simulation.setLoan(null);

        assertNull(simulation.getSimulationId());
        assertNull(simulation.getLoanAmount());
        assertNull(simulation.getCurrency());
        assertNull(simulation.getInterestRate());
        assertNull(simulation.getTerm());
        assertNull(simulation.getInstallment());
        assertNull(simulation.getTotalPayment());
        assertNull(simulation.getAcceptance());
        assertNull(simulation.getSimulationDate());
        assertNull(simulation.getDisbursementDate());
        assertNull(simulation.getClient());
        assertNull(simulation.getLoan());
    }

    @Test
    void testSimulationDefaultConstructor() {
        Simulation simulation = new Simulation();

        assertNull(simulation.getSimulationId());
        assertNull(simulation.getLoanAmount());
        assertNull(simulation.getCurrency());
        assertNull(simulation.getInterestRate());
        assertNull(simulation.getTerm());
        assertNull(simulation.getInstallment());
        assertNull(simulation.getTotalPayment());
        assertNull(simulation.getAcceptance());
        assertNull(simulation.getSimulationDate());
        assertNull(simulation.getDisbursementDate());
        assertNull(simulation.getClient());
        assertNull(simulation.getLoan());
    }
}
