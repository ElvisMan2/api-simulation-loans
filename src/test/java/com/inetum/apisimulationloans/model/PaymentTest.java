package com.inetum.apisimulationloans.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    @Test
    void testPaymentFullCoverage() {
        // Arrange
        Loan loan = new Loan();
        loan.setLoanId(500L);

        Payment payment = new Payment();

        LocalDate dueDate = LocalDate.of(2026, 1, 15);

        // Act
        payment.setInstallmentId(100L);
        payment.setPaymentNumber(1);
        payment.setCurrency("PEN");
        payment.setInstallment(500.0);
        payment.setAmortization(450.0);
        payment.setInterest(50.0);
        payment.setDueDate(dueDate);
        payment.setCapitalBalance(9550.0);
        payment.setLoan(loan);

        // Assert
        assertEquals(100L, payment.getInstallmentId());
        assertEquals(1, payment.getPaymentNumber());
        assertEquals("PEN", payment.getCurrency());
        assertEquals(500.0, payment.getInstallment());
        assertEquals(450.0, payment.getAmortization());
        assertEquals(50.0, payment.getInterest());
        assertEquals(dueDate, payment.getDueDate());
        assertEquals(9550.0, payment.getCapitalBalance());
        assertEquals(loan, payment.getLoan());
    }

    @Test
    void testPaymentWithNullLoan() {
        Payment payment = new Payment();

        payment.setLoan(null);

        assertNull(payment.getLoan());
    }

    @Test
    void testPaymentNullValues() {
        Payment payment = new Payment();

        payment.setInstallmentId(null);
        payment.setPaymentNumber(null);
        payment.setCurrency(null);
        payment.setInstallment(null);
        payment.setAmortization(null);
        payment.setInterest(null);
        payment.setDueDate(null);
        payment.setCapitalBalance(null);

        assertNull(payment.getInstallmentId());
        assertNull(payment.getPaymentNumber());
        assertNull(payment.getCurrency());
        assertNull(payment.getInstallment());
        assertNull(payment.getAmortization());
        assertNull(payment.getInterest());
        assertNull(payment.getDueDate());
        assertNull(payment.getCapitalBalance());
    }

    @Test
    void testPaymentDefaultConstructor() {
        Payment payment = new Payment();

        assertNull(payment.getInstallmentId());
        assertNull(payment.getPaymentNumber());
        assertNull(payment.getCurrency());
        assertNull(payment.getInstallment());
        assertNull(payment.getAmortization());
        assertNull(payment.getInterest());
        assertNull(payment.getDueDate());
        assertNull(payment.getCapitalBalance());
        assertNull(payment.getLoan());
    }
}
