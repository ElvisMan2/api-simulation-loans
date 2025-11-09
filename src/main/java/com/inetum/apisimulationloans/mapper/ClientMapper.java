package com.inetum.apisimulationloans.mapper;

import com.inetum.apisimulationloans.dto.ClientDTO;
import com.inetum.apisimulationloans.model.Client;

import java.time.LocalDateTime;

public class ClientMapper {
    public static Client toEntity(ClientDTO dto) {
        Client client = new Client();
        client.setFirstName(dto.getFirstName());
        client.setPaternalLastName(dto.getPaternalLastName());
        client.setMaternalLastName(dto.getMaternalLastName());
        client.setCurrencyOfIncome(dto.getCurrencyOfIncome());
        client.setMonthlyIncome(dto.getMonthlyIncome());
        client.setCreationDate(LocalDateTime.now());
        return client;
    }

    public static ClientDTO toDto(Client client) {
        ClientDTO dto = new ClientDTO();
        dto.setFirstName(client.getFirstName());
        dto.setPaternalLastName(client.getPaternalLastName());
        dto.setMaternalLastName(client.getMaternalLastName());
        dto.setCurrencyOfIncome(client.getCurrencyOfIncome());
        dto.setMonthlyIncome(client.getMonthlyIncome());
        return dto;
    }

}

