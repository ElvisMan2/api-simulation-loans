package com.inetum.apisimulationloans.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.inetum.apisimulationloans.dto.ClientDTO;
import com.inetum.apisimulationloans.service.ClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClientController.class)
class ClientControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClientService clientService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateClient() throws Exception {
        // Arrange
        ClientDTO inputDTO = ClientDTO.builder()
                .clientId(2L)
                .firstName("Luis")
                .paternalLastName("Martinez")
                .maternalLastName("Lopez")
                .currencyOfIncome("PEN")
                .monthlyIncome(40000.0)
                .build();

        when(clientService.createClient(Mockito.any(ClientDTO.class))).thenReturn(inputDTO);

        // Act & Assert
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("User created successfully"))
                .andExpect(jsonPath("$[1].clientId").value(2L))
                .andExpect(jsonPath("$[1].firstName").value("Luis"))
                .andExpect(jsonPath("$[1].monthlyIncome").value(40000.0));
    }

    @Test
    void testGetClientById() throws Exception {
        // Arrange
        Long clientId = 1L;
        ClientDTO dto = ClientDTO.builder()
                .clientId(clientId)
                .firstName("Ana")
                .paternalLastName("Ramirez")
                .maternalLastName("Torres")
                .currencyOfIncome("USD")
                .monthlyIncome(50000.0)
                .build();

        when(clientService.getClientById(clientId)).thenReturn(dto);

        // Act & Assert
        mockMvc.perform(get("/api/clients/{id}", clientId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.clientId").value(clientId))
                .andExpect(jsonPath("$.firstName").value("Ana"))
                .andExpect(jsonPath("$.monthlyIncome").value(50000.0));
    }

    @Test
    void testHandleGeneralException() throws Exception {
        // Arrange: DTO válido pero el servicio lanza una excepción general
        ClientDTO inputDTO = ClientDTO.builder()
                .clientId(3L)
                .firstName("Carlos")
                .paternalLastName("Perez")
                .maternalLastName("Gomez")
                .currencyOfIncome("USD")
                .monthlyIncome(30000.0)
                .build();

        when(clientService.createClient(Mockito.any(ClientDTO.class)))
                .thenThrow(new RuntimeException("Unexpected failure"));

        // Act & Assert
        mockMvc.perform(post("/api/clients")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDTO)))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.error").value("Internal Server Error"))
                .andExpect(jsonPath("$.message").value("Unexpected failure"));
    }
}
