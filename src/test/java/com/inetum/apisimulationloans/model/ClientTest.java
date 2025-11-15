package com.inetum.apisimulationloans.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ClientTest {

    @Test
    void testClientModelFullCoverage() {
        // Arrange
        LocalDateTime now = LocalDateTime.now();

        Client client = new Client();
        client.setClientId(1L);
        client.setFirstName("Carlos");
        client.setPaternalLastName("Ramírez");
        client.setMaternalLastName("Lopez");
        client.setCurrencyOfIncome("USD");
        client.setMonthlyIncome(4000.0);
        client.setCreationDate(now);

        // Relaciones
        Simulation sim = new Simulation();
        sim.setSimulationId(100L);
        sim.setClient(client);

        Loan loan = new Loan();
        loan.setLoanId(200L);
        loan.setClient(client);

        client.setSimulations(List.of(sim));
        client.setLoans(List.of(loan));

        // Assert getters y consistencia
        assertEquals(1L, client.getClientId());
        assertEquals("Carlos", client.getFirstName());
        assertEquals("Ramírez", client.getPaternalLastName());
        assertEquals("Lopez", client.getMaternalLastName());
        assertEquals("USD", client.getCurrencyOfIncome());
        assertEquals(4000.0, client.getMonthlyIncome());
        assertEquals(now, client.getCreationDate());

        // Relaciones directas
        assertEquals(1, client.getSimulations().size());
        assertEquals(1, client.getLoans().size());
        assertEquals(client, client.getSimulations().get(0).getClient());
        assertEquals(client, client.getLoans().get(0).getClient());
    }


    @Test
    void testClientWithEmptyLists() {
        Client client = new Client();

        client.setSimulations(Collections.emptyList());
        client.setLoans(Collections.emptyList());

        assertNotNull(client.getSimulations());
        assertNotNull(client.getLoans());
        assertEquals(0, client.getSimulations().size());
        assertEquals(0, client.getLoans().size());
    }


    @Test
    void testClientNullValues() {
        Client client = new Client();

        client.setFirstName(null);
        client.setPaternalLastName(null);
        client.setMaternalLastName(null);
        client.setCurrencyOfIncome(null);
        client.setCreationDate(null);

        assertNull(client.getFirstName());
        assertNull(client.getPaternalLastName());
        assertNull(client.getMaternalLastName());
        assertNull(client.getCurrencyOfIncome());
        assertNull(client.getCreationDate());
    }


    @Test
    void testClientDefaultConstructor() {
        Client client = new Client();

        assertNull(client.getClientId());
        assertNull(client.getFirstName());
        assertNull(client.getPaternalLastName());
        assertNull(client.getMaternalLastName());
        assertNull(client.getCurrencyOfIncome());
        assertNull(client.getMonthlyIncome());
        assertNull(client.getCreationDate());

        // Las listas se inicializan vacías, no como null
        assertNotNull(client.getSimulations());
        assertNotNull(client.getLoans());
        assertEquals(0, client.getSimulations().size());
        assertEquals(0, client.getLoans().size());
    }

}
