package com.inetum.apisimulationloans.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClientDTOTest {

    @Test
    void testGettersAndSetters() {
        ClientDTO dto = new ClientDTO();

        dto.setFirstName("Juan");
        dto.setPaternalLastName("Perez");
        dto.setMaternalLastName("Lopez");
        dto.setCurrencyOfIncome("PEN");
        dto.setMonthlyIncome(2500.50);

        assertEquals("Juan", dto.getFirstName());
        assertEquals("Perez", dto.getPaternalLastName());
        assertEquals("Lopez", dto.getMaternalLastName());
        assertEquals("PEN", dto.getCurrencyOfIncome());
        assertEquals(2500.50, dto.getMonthlyIncome());
    }

    @Test
    void testNoArgsConstructor() {
        ClientDTO dto = new ClientDTO();
        assertNotNull(dto);
    }

}
