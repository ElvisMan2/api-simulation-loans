package com.inetum.apisimulationloans.mapper;

import com.inetum.apisimulationloans.dto.ClientDTO;
import com.inetum.apisimulationloans.model.Client;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.assertj.core.api.Assertions.assertThat;

class ClientMapperTest {

	private final ClientMapper mapper = Mappers.getMapper(ClientMapper.class);

	@Test
	void toEntity_shouldMapFields() {
		ClientDTO dto = ClientDTO.builder()
				.clientId(123L)
				.firstName("Juan")
				.paternalLastName("Perez")
				.maternalLastName("Gomez")
				.currencyOfIncome("USD")
				.monthlyIncome(2500.0)
				.build();

		Client entity = mapper.toEntity(dto);

		assertThat(entity).isNotNull();
		assertThat(entity.getClientId()).isEqualTo(dto.getClientId());
		assertThat(entity.getFirstName()).isEqualTo(dto.getFirstName());
		assertThat(entity.getPaternalLastName()).isEqualTo(dto.getPaternalLastName());
		assertThat(entity.getMaternalLastName()).isEqualTo(dto.getMaternalLastName());
		assertThat(entity.getCurrencyOfIncome()).isEqualTo(dto.getCurrencyOfIncome());
		assertThat(entity.getMonthlyIncome()).isEqualTo(dto.getMonthlyIncome());
	}

	@Test
	void toDto_shouldMapFields() {
		Client entity = new Client();
		entity.setClientId(321L);
		entity.setFirstName("Ana");
		entity.setPaternalLastName("Lopez");
		entity.setMaternalLastName("Diaz");
		entity.setCurrencyOfIncome("EUR");
		entity.setMonthlyIncome(4200.5);

		ClientDTO dto = mapper.toDto(entity);

		assertThat(dto).isNotNull();
		assertThat(dto.getClientId()).isEqualTo(entity.getClientId());
		assertThat(dto.getFirstName()).isEqualTo(entity.getFirstName());
		assertThat(dto.getPaternalLastName()).isEqualTo(entity.getPaternalLastName());
		assertThat(dto.getMaternalLastName()).isEqualTo(entity.getMaternalLastName());
		assertThat(dto.getCurrencyOfIncome()).isEqualTo(entity.getCurrencyOfIncome());
		assertThat(dto.getMonthlyIncome()).isEqualTo(entity.getMonthlyIncome());
	}

	@Test
	void nullMapping_shouldReturnNull() {
		assertThat(mapper.toEntity(null)).isNull();
		assertThat(mapper.toDto(null)).isNull();
	}
}
