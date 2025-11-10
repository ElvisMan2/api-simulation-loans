package com.inetum.apisimulationloans.service;

import com.inetum.apisimulationloans.dto.ClientDTO;
import com.inetum.apisimulationloans.exception.ClientNotFoundException;
import com.inetum.apisimulationloans.mapper.ClientMapper;
import com.inetum.apisimulationloans.model.Client;
import com.inetum.apisimulationloans.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.NoSuchElementException;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Client createClient(ClientDTO clientDTO) {
        Client client = ClientMapper.toEntity(clientDTO);
        client.setCreationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        return clientRepository.save(client);
    }

    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));

        // Aquí usamos el mapper existente
        return ClientMapper.toDto(client);
    }

    public Client getClientByIdOrThrow(Long id) {
        return clientRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Client not found with id: " + id));
    }


}


