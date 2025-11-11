package com.inetum.apisimulationloans.mapper;

import com.inetum.apisimulationloans.dto.SimulationDTO;
import com.inetum.apisimulationloans.model.Simulation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SimulationMapper {

    @Mapping(source = "id", target = "simulationId")
    @Mapping(source = "monthlyPayment", target = "installment")
    @Mapping(source = "approved", target = "acceptance")
    @Mapping(source = "createdAt", target = "simulationDate")
    @Mapping(source = "clientId", target = "client.clientId")
    Simulation toEntity(SimulationDTO dto);

    @Mapping(source = "simulationId", target = "id")
    @Mapping(source = "installment", target = "monthlyPayment")
    @Mapping(source = "acceptance", target = "approved")
    @Mapping(source = "simulationDate", target = "createdAt")
    @Mapping(source = "client.clientId", target = "clientId")
    SimulationDTO toDto(Simulation entity);
}
