package com.inetum.apisimulationloans.service;

import com.inetum.apisimulationloans.dto.ClientDTO;
import com.inetum.apisimulationloans.exception.ClientNotFoundException;
import com.inetum.apisimulationloans.mapper.ClientMapper;
import com.inetum.apisimulationloans.model.Client;
import com.inetum.apisimulationloans.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.beans.factory.annotation.Autowired;

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

    @Autowired
    private ClientMapper clientMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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

//        Client mappedEntity = new Client();
//        mappedEntity.setClientId(1L);
//        mappedEntity.setFirstName("Juan");
//        mappedEntity.setPaternalLastName("Pérez");
//        mappedEntity.setMaternalLastName("Gómez");
//        mappedEntity.setCurrencyOfIncome("soles");
//        mappedEntity.setMonthlyIncome(30000.0);
        Client mappedEntity= clientMapper.toEntity(inputDTO);

        // Establecemos la fecha esperada
        LocalDateTime expectedCreationDate = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS);
        mappedEntity.setCreationDate(expectedCreationDate);

        Client savedEntity = new Client();
        savedEntity.setClientId(1L);
        savedEntity.setFirstName("Juan");
        savedEntity.setPaternalLastName("Pérez");
        savedEntity.setMaternalLastName("Gómez");
        savedEntity.setCurrencyOfIncome("soles");
        savedEntity.setMonthlyIncome(30000.00);
        savedEntity.setCreationDate(expectedCreationDate);

        //ClientDTO outputDTO = inputDTO; // Podrías simular otro objeto si se desea

        //when(clientMapper.toEntity(inputDTO)).thenReturn(mappedEntity);
        when(clientRepository.save(mappedEntity)).thenReturn(savedEntity);
        //when(clientMapper.toDto(savedEntity)).thenReturn(outputDTO);

        // Act
        ClientDTO result = clientService.createClient(inputDTO);

        // Assert
        assertNotNull(result);
        assertEquals(inputDTO.getClientId(), result.getClientId());
        assertEquals(inputDTO.getFirstName(), result.getFirstName());
        assertEquals(inputDTO.getMonthlyIncome(), result.getMonthlyIncome());

        verify(clientMapper).toEntity(inputDTO);
        verify(clientRepository).save(mappedEntity);
        verify(clientMapper).toDto(savedEntity);
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

        ClientDTO expectedDto = ClientDTO.builder()
                .clientId(clientId)
                .firstName("Ana")
                .paternalLastName("Ramirez")
                .maternalLastName("Torres")
                .currencyOfIncome("USD")
                .monthlyIncome(50000.0)
                .build();

        when(clientRepository.findById(clientId)).thenReturn(Optional.of(client));
        //when(clientMapper.toDto(client)).thenReturn(expectedDto);

        // Act
        ClientDTO result = clientService.getClientById(clientId);

        // Assert
        assertNotNull(result);
        assertEquals(expectedDto.getClientId(), result.getClientId());
        assertEquals(expectedDto.getFirstName(), result.getFirstName());
        assertEquals(expectedDto.getMonthlyIncome(), result.getMonthlyIncome());

        verify(clientRepository).findById(clientId);
        verify(clientMapper).toDto(client);
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
        //verifyNoInteractions(clientMapper);
    }


}