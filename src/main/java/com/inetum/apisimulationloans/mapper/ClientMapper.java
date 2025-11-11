package com.inetum.apisimulationloans.mapper;

import com.inetum.apisimulationloans.dto.ClientDTO;
import com.inetum.apisimulationloans.model.Client;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientMapper {

    Client toEntity(ClientDTO dto);

    ClientDTO toDto(Client entity);
}

