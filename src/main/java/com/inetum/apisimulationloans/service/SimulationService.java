package com.inetum.apisimulationloans.service;

import com.inetum.apisimulationloans.dto.ClientDTO;
import com.inetum.apisimulationloans.dto.SimulationRequest;
import com.inetum.apisimulationloans.dto.SimulationResponse;
import com.inetum.apisimulationloans.mapper.ClientMapper;
import com.inetum.apisimulationloans.mapper.SimulationMapper;
import com.inetum.apisimulationloans.model.Client;
import com.inetum.apisimulationloans.model.Simulation;
import com.inetum.apisimulationloans.repository.ClientRepository;
import com.inetum.apisimulationloans.repository.SimulationRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class SimulationService {

    //inyecccion de dependencias:
    private final SimulationRepository simulationRepository;
    private final ClientRepository clientRepository;

    private final SimulationMapper simulationMapper;
    private final ClientMapper clientMapper;

    public SimulationService(SimulationRepository simulationRepository,
                             ClientRepository clientRepository,
                             SimulationMapper simulationMapper,
                             ClientMapper clientMapper) {
        this.simulationRepository = simulationRepository;
        this.clientRepository = clientRepository;
        this.simulationMapper = simulationMapper;
        this.clientMapper = clientMapper;
    }


    //servicios

    public SimulationResponse createSimulation(Long clientId, SimulationRequest request) {

        //se obtiene el cliente de la base de datos
        Client client= clientRepository.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("Client not found with ID: " + clientId));

        ClientDTO clientDTO = clientMapper.toDto(client);

        //se obtiene los datos de la simulacion
        double loanAmount = request.getLoanAmount();
        double annualRate = request.getInterestRate();
        int term = request.getTerm();
        double monthlyRate = annualRate / 12 / 100;
        LocalDate disbursementDate=request.getDisbursementDate();

        double numerator = monthlyRate * Math.pow(1 + monthlyRate, term);
        double denominator = Math.pow(1 + monthlyRate, term) - 1;
        double rawMonthlyPayment = loanAmount * (numerator / denominator);
        double rawTotalPayment = rawMonthlyPayment * term;

        // Redondeo con BigDecimal
        BigDecimal monthlyPayment = BigDecimal.valueOf(rawMonthlyPayment)
                .setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalPayment = BigDecimal.valueOf(rawTotalPayment)
                .setScale(2, RoundingMode.HALF_UP);

        double maxAffordable = clientDTO.getMonthlyIncome() * 0.5;
        boolean approved = monthlyPayment.doubleValue() <= maxAffordable;

        // Guardar en base de datos
        Simulation sim = new Simulation();
        sim.setLoanAmount(loanAmount);
        sim.setCurrency(request.getCurrency());
        sim.setInterestRate(annualRate);
        sim.setTerm(term);
        sim.setInstallment(monthlyPayment.doubleValue());
        sim.setTotalPayment(totalPayment.doubleValue());
        sim.setAcceptance(approved);
        sim.setSimulationDate(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        sim.setClient(client);
        sim.setDisbursementDate(disbursementDate);

        Simulation saved = simulationRepository.save(sim);

        return simulationMapper.toDto(saved);
    }

    public List<SimulationResponse> getSimulationsByClientId(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("Client not found with ID: " + clientId));

        return client.getSimulations()
                .stream()
                .map(simulationMapper::toDto)
                .toList();
    }

}
