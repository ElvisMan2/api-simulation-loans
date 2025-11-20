package com.inetum.apisimulationloans.service;

import com.inetum.apisimulationloans.dto.ClientDTO;
import com.inetum.apisimulationloans.exception.ClientNotFoundException;
import com.inetum.apisimulationloans.mapper.ClientMapper;
import com.inetum.apisimulationloans.model.Client;
import com.inetum.apisimulationloans.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class ClientService {

    //inyecccion de dependencias:
    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    public ClientService(ClientRepository clientRepository, ClientMapper clientMapper) {
        this.clientRepository = clientRepository;
        this.clientMapper = clientMapper;
    }

    //servicios

    public ClientDTO createClient(ClientDTO clientDTO) {

        Client client = clientMapper.toEntity(clientDTO);//lo convierto a entidad
        client.setCreationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));

        return clientMapper.toDto(clientRepository.save(client));
    }

    public ClientDTO getClientById(Long id) {
        Client client = clientRepository.findById(id)
                .orElseThrow(() -> new ClientNotFoundException(id));
        return clientMapper.toDto(client);
    }

    public boolean existsClientByNames(String firstName, String paternalLastName, String maternalLastName) {
            return clientRepository.existsByFirstNameAndPaternalLastNameAndMaternalLastName(firstName, paternalLastName, maternalLastName);
    }

    // Nuevo método: obtener cliente por nombres y devolver su DTO (o null si no existe)
    public ClientDTO getClientByNames(String firstName, String paternalLastName, String maternalLastName) {
        Optional<Client> opt = clientRepository.findByFirstNameAndPaternalLastNameAndMaternalLastName(firstName, paternalLastName, maternalLastName);
        return opt.map(clientMapper::toDto).orElse(null);
    }

}
