package com.inetum.apisimulationloans.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class SimulationResponseTest {

    @Test
    void testGettersAndSetters() {
        SimulationResponse res = new SimulationResponse();

        res.setMonthlyPayment(Double.valueOf(500.55));
        res.setTotalPayment(Double.valueOf(12000.40));
        res.setCurrency("PEN");

        assertTrue(Double.valueOf(500.55).compareTo(res.getMonthlyPayment()) == 0);
        assertTrue(Double.valueOf(12000.40).compareTo(res.getTotalPayment()) == 0);
        assertEquals("PEN", res.getCurrency());
    }

    @Test
    void testNoArgsConstructor() {
        SimulationResponse res = new SimulationResponse();
        assertNotNull(res);
    }
}
