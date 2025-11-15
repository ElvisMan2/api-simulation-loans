package com.inetum.apisimulationloans.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SimulationRequestTest {

    @Test
    void testGettersAndSetters() {
        SimulationRequest req = new SimulationRequest();

        req.setLoanAmount(10000.0);
        req.setInterestRate(12.5);
        req.setTerm(24);
        req.setCurrency("PEN");

        assertEquals(10000.0, req.getLoanAmount());
        assertEquals(12.5, req.getInterestRate());
        assertEquals(24, req.getTerm());
        assertEquals("PEN", req.getCurrency());
    }

    @Test
    void testNoArgsConstructor() {
        SimulationRequest req = new SimulationRequest();
        assertNotNull(req);
    }
}
