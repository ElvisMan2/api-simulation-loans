package com.inetum.apisimulationloans.service;

import com.inetum.apisimulationloans.dto.ClientDTO;
import com.inetum.apisimulationloans.exception.ClientNotFoundException;
import com.inetum.apisimulationloans.mapper.ClientMapper;
import com.inetum.apisimulationloans.model.Client;
import com.inetum.apisimulationloans.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;



class ClientServiceTest {

    @InjectMocks
    private ClientService clientService;

    @Mock
    private ClientRepository clientRepository;

    private ClientMapper clientMapper; // Usamos la implementación real

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clientMapper = Mappers.getMapper(ClientMapper.class);
        clientService = new ClientService(clientRepository, clientMapper); // Inyección manual
    }

    @Test
    void testCreateClient() {
        // Arrange
        ClientDTO inputDTO = ClientDTO.builder()
                .clientId(1L)
                .firstName("Juan")
                .paternalLastName("Pérez")
                .maternalLastName("Gómez")
                .currencyOfIncome("soles")
                .monthlyIncome(30000.00)
                .build();

        Client mappedEntity = clientMapper.toEntity(inputDTO);

        LocalDateTime expectedCreationDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        mappedEntity.setCreationDate(expectedCreationDate);

        Client savedEntity = new Client();
        savedEntity.setClientId(mappedEntity.getClientId());
        savedEntity.setFirstName(mappedEntity.getFirstName());
        savedEntity.setPaternalLastName(mappedEntity.getPaternalLastName());
        savedEntity.setMaternalLastName(mappedEntity.getMaternalLastName());
        savedEntity.setCurrencyOfIncome(mappedEntity.getCurrencyOfIncome());
        savedEntity.setMonthlyIncome(mappedEntity.getMonthlyIncome());
        savedEntity.setCreationDate(expectedCreationDate);

        when(clientRepository.save(any(Client.class))).thenReturn(savedEntity);

        // Act
        ClientDTO result = clientService.createClient(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(inputDTO.getClientId(), result.getClientId());
        assertEquals(inputDTO.getFirstName(), result.getFirstName());
        assertEquals(inputDTO.getMonthlyIncome(), result.getMonthlyIncome());

        verify(clientRepository).save(any(Client.class));
    }


    @Test
    void testGetClientById_whenClientExists_returnsDto() {
        // Arrange
        Long clientId = 1L;

        Client client = new Client();
        client.setClientId(clientId);
        client.setFirstName("Ana");
        client.setPaternalLastName("Ramirez");
        client.setMaternalLastName("Torres");
        client.setCurrencyOfIncome("USD");
        client.setMonthlyIncome(50000.0);

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));

        // Act
        ClientDTO result = clientService.getClientById(clientId);

        // Assert
        assertNotNull(result);
        assertEquals(client.getClientId(), result.getClientId());
        assertEquals(client.getFirstName(), result.getFirstName());
        assertEquals(client.getPaternalLastName(), result.getPaternalLastName());
        assertEquals(client.getMaternalLastName(), result.getMaternalLastName());
        assertEquals(client.getCurrencyOfIncome(), result.getCurrencyOfIncome());
        assertEquals(client.getMonthlyIncome(), result.getMonthlyIncome());

        verify(clientRepository).findById(clientId);
    }


    @Test
    void testGetClientById_whenClientNotFound_throwsException() {
        // Arrange
        Long clientId = 99L;

        when(clientRepository.findById(clientId)).thenReturn(Optional.empty());

        // Act + Assert
        ClientNotFoundException thrown = assertThrows(
                ClientNotFoundException.class,
                () -> clientService.getClientById(clientId),
                "Expected getClientById() to throw, but it didn't"
        );

        assertTrue(thrown.getMessage().contains(clientId.toString()));
        verify(clientRepository).findById(clientId);
    }


}