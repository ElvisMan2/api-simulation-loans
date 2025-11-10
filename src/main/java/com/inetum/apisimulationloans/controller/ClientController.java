package com.inetum.apisimulationloans.controller;

import com.inetum.apisimulationloans.dto.ClientDTO;
import com.inetum.apisimulationloans.model.Client;
import com.inetum.apisimulationloans.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/clients")
public class ClientController {

    private final ClientService clientService;

    public ClientController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        ClientDTO clientDTO = clientService.getClientById(id);
        return ResponseEntity.ok(clientDTO);
    }



    @PostMapping
    public ResponseEntity<Client> createClient(@Valid @RequestBody ClientDTO clientDTO) {
        Client createdClient = clientService.createClient(clientDTO);
        return ResponseEntity.ok(createdClient);
    }
}

