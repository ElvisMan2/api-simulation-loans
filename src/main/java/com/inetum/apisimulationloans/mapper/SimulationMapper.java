package com.inetum.apisimulationloans.mapper;

import com.inetum.apisimulationloans.dto.SimulationResponse;
import com.inetum.apisimulationloans.model.Simulation;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SimulationMapper {

    
    @Mapping(source = "monthlyPayment", target = "installment")
    @Mapping(source = "approved", target = "acceptance")
    @Mapping(source = "createdAt", target = "simulationDate")
    @Mapping(source = "clientId", target = "client.clientId")
    Simulation toEntity(SimulationResponse dto);


    @Mapping(source = "installment", target = "monthlyPayment")
    @Mapping(source = "acceptance", target = "approved")
    @Mapping(source = "simulationDate", target = "createdAt")
    @Mapping(source = "client.clientId", target = "clientId")
    SimulationResponse toDto(Simulation entity);
}
