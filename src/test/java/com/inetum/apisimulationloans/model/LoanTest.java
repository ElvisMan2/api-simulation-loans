package com.inetum.apisimulationloans.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class LoanTest {

    @Test
    void testLoanModelFullCoverage() {
        // Arrange
        Client client = new Client();
        client.setClientId(10L);

        Simulation simulation = new Simulation();
        simulation.setSimulationId(20L);

        Payment payment1 = new Payment();
        payment1.setInstallmentId(1L);

        Payment payment2 = new Payment();
        payment2.setInstallmentId(2L);

        LocalDateTime now = LocalDateTime.now();
        LocalDate disbursementDate = LocalDate.of(2025, 12, 10);

        Loan loan = new Loan();

        // Act
        loan.setLoanId(100L);
        loan.setLoanAmount(12000.0);
        loan.setInterestRate(6.0);
        loan.setTerm(24);
        loan.setInstallment(530.0);
        loan.setStatus(1);
        loan.setCurrency("PEN");
        loan.setCreationDate(now);
        loan.setDisbursementDate(disbursementDate);
        loan.setClient(client);
        loan.setSimulation(simulation);
        loan.setPayment(List.of(payment1, payment2));

        // Relaciones inversas (si existen en modelo)
        payment1.setLoan(loan);
        payment2.setLoan(loan);

        // Assert - valores directos
        assertEquals(100L, loan.getLoanId());
        assertEquals(12000.0, loan.getLoanAmount());
        assertEquals(6.0, loan.getInterestRate());
        assertEquals(24, loan.getTerm());
        assertEquals(530.0, loan.getInstallment());
        assertEquals(1, loan.getStatus());
        assertEquals("PEN", loan.getCurrency());
        assertEquals(now, loan.getCreationDate());
        assertEquals(disbursementDate, loan.getDisbursementDate());

        // Relaciones
        assertEquals(client, loan.getClient());
        assertEquals(simulation, loan.getSimulation());
        assertEquals(2, loan.getPayment().size());
        assertEquals(loan, payment1.getLoan());
        assertEquals(loan, payment2.getLoan());
    }

    @Test
    void testLoanWithEmptyPayments() {
        Loan loan = new Loan();

        loan.setPayment(Collections.emptyList());

        assertNotNull(loan.getPayment());
        assertEquals(0, loan.getPayment().size());
    }

    @Test
    void testLoanNullValues() {
        Loan loan = new Loan();

        loan.setLoanAmount(null);
        loan.setInterestRate(null);
        loan.setTerm(null);
        loan.setInstallment(null);
        loan.setCurrency(null);
        loan.setCreationDate(null);
        loan.setDisbursementDate(null);
        loan.setClient(null);
        loan.setSimulation(null);
        loan.setPayment(null);

        assertNull(loan.getLoanAmount());
        assertNull(loan.getInterestRate());
        assertNull(loan.getTerm());
        assertNull(loan.getInstallment());
        assertNull(loan.getCurrency());
        assertNull(loan.getCreationDate());
        assertNull(loan.getDisbursementDate());
        assertNull(loan.getClient());
        assertNull(loan.getSimulation());
        assertNull(loan.getPayment());
    }

    @Test
    void testLoanDefaultConstructor() {
        Loan loan = new Loan();

        assertNull(loan.getLoanId());
        assertNull(loan.getLoanAmount());
        assertNull(loan.getInterestRate());
        assertNull(loan.getTerm());
        assertNull(loan.getInstallment());
        assertNull(loan.getStatus());
        assertNull(loan.getCurrency());
        assertNull(loan.getCreationDate());
        assertNull(loan.getDisbursementDate());
        assertNull(loan.getClient());
        assertNull(loan.getSimulation());

        // La lista payment NO es null: se inicializa vacía
        assertNotNull(loan.getPayment());
        assertEquals(0, loan.getPayment().size());
    }

}
