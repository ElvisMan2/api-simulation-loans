package com.inetum.apisimulationloans.controller;

import com.inetum.apisimulationloans.dto.ClientDTO;
import com.inetum.apisimulationloans.service.ClientService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/api/clients")
public class ClientController {

    //inyeccion de dependencias
    private final ClientService clientService;


    public ClientController(ClientService clientService) {
        this.clientService = clientService;

    }

    //controllers

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        ClientDTO clientDTO = clientService.getClientById(id);

        return ResponseEntity.ok(clientDTO);
    }

    @PostMapping
    public ResponseEntity<?> createClient(@Valid @RequestBody ClientDTO clientDTO) {

        if(clientService.existsClientByNames(clientDTO.getFirstName(), clientDTO.getPaternalLastName(), clientDTO.getMaternalLastName())) {
            // Si ya existe, devolver el arreglo solicitado con el DTO existente
            ClientDTO existing = clientService.getClientByNames(clientDTO.getFirstName(), clientDTO.getPaternalLastName(), clientDTO.getMaternalLastName());
            List<Object> response = new ArrayList<>();
            response.add("User already exist");
            response.add(existing);
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }else {
            List<Object> response = new ArrayList<>();
            response.add("User created successfully");
            response.add(clientService.createClient(clientDTO));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
    }
}
