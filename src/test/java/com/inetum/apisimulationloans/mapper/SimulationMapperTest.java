package com.inetum.apisimulationloans.mapper;

import com.inetum.apisimulationloans.dto.SimulationResponse;
import com.inetum.apisimulationloans.model.Client;
import com.inetum.apisimulationloans.model.Simulation;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SimulationMapperTest {

    @Autowired
    private SimulationMapper simulationMapper;

    @Test
    void shouldMapToEntityCorrectly() {
        SimulationResponse dto = new SimulationResponse();
        dto.setSimulationId(123L);
        dto.setMonthlyPayment(250.75);
        dto.setApproved(true);
        dto.setCreatedAt(LocalDateTime.of(2023, 5, 10, 12, 30));
        dto.setClientId(45L);

        Simulation entity = simulationMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getSimulationId()).isEqualTo(123L);
        assertThat(entity.getInstallment()).isEqualTo(250.75);
        assertThat(entity.getAcceptance()).isTrue();
        assertThat(entity.getSimulationDate()).isEqualTo(LocalDateTime.of(2023, 5, 10, 12, 30));
        assertThat(entity.getClient()).isNotNull();
        assertThat(entity.getClient().getClientId()).isEqualTo(45L);
    }

    @Test
    void shouldMapToDtoCorrectly() {
        Client client = new Client();
        client.setClientId(45L);

        Simulation simulation = new Simulation();
        simulation.setSimulationId(123L);
        simulation.setInstallment(250.75);
        simulation.setAcceptance(true);
        simulation.setSimulationDate(LocalDateTime.of(2023, 5, 10, 12, 30));
        simulation.setClient(client);

        SimulationResponse dto = simulationMapper.toDto(simulation);

        assertThat(dto).isNotNull();
        assertThat(dto.getSimulationId()).isEqualTo(123L);
        assertThat(dto.getMonthlyPayment()).isEqualTo(250.75);
        assertThat(dto.getApproved()).isTrue();
        assertThat(dto.getCreatedAt()).isEqualTo(LocalDateTime.of(2023, 5, 10, 12, 30));
        assertThat(dto.getClientId()).isEqualTo(45L);
    }
}
