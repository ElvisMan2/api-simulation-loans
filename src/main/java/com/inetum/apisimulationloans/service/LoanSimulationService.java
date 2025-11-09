package com.inetum.apisimulationloans.service;

import com.inetum.apisimulationloans.dto.LoanSimulationRequest;
import com.inetum.apisimulationloans.dto.SimulationDTO;
import com.inetum.apisimulationloans.model.Client;
import com.inetum.apisimulationloans.model.Simulation;
import com.inetum.apisimulationloans.repository.ClientRepository;
import com.inetum.apisimulationloans.repository.SimulationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class LoanSimulationService {

    //inyecccion de dependencias:
    private final SimulationRepository simulationRepository;

    private final ClientRepository clientRepository;

    public LoanSimulationService(SimulationRepository simulationRepository, ClientRepository clientRepository) {
        this.simulationRepository = simulationRepository;
        this.clientRepository = clientRepository;
    }


    //servicios

    public SimulationDTO simulateAndSave(Client client, LoanSimulationRequest request) {
        double loanAmount = request.getLoanAmount();
        double annualRate = request.getInterestRate();
        int term = request.getTerm();
        double monthlyRate = annualRate / 12 / 100;

        double numerator = monthlyRate * Math.pow(1 + monthlyRate, term);
        double denominator = Math.pow(1 + monthlyRate, term) - 1;
        double monthlyPayment = loanAmount * (numerator / denominator);
        double totalPayment = monthlyPayment * term;

        double maxAffordable = client.getMonthlyIncome() * 0.4;
        boolean approved = monthlyPayment <= maxAffordable;

        // Guardar en base de datos
        Simulation sim = new Simulation();
        sim.setLoanAmount(loanAmount);
        sim.setCurrency(request.getCurrency());
        sim.setInterestRate(annualRate);
        sim.setTerm(term);
        sim.setInstallment(monthlyPayment);
        sim.setTotalPayment(totalPayment);
        sim.setAcceptance(approved);
        sim.setSimulationDate(LocalDateTime.now());
        sim.setClient(client);

        Simulation saved = simulationRepository.save(sim);

        // Convertir a DTO para devolver
        SimulationDTO dto = new SimulationDTO();
        dto.setId(saved.getSimulationId());
        dto.setLoanAmount(saved.getLoanAmount());
        dto.setCurrency(saved.getCurrency());
        dto.setInterestRate(saved.getInterestRate());
        dto.setTerm(saved.getTerm());
        dto.setMonthlyPayment(saved.getInstallment());
        dto.setTotalPayment(saved.getTotalPayment());
        dto.setApproved(saved.getAcceptance());
        dto.setCreatedAt(saved.getSimulationDate());
        dto.setClientId(client.getClientId());

        return dto;
    }

    public List<SimulationDTO> getSimulationsByClientId(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new NoSuchElementException("Client not found with ID: " + clientId));

        return client.getSimulations()
                .stream()
                .map(sim -> {
                    SimulationDTO dto = new SimulationDTO();
                    dto.setId(sim.getSimulationId());
                    dto.setLoanAmount(sim.getLoanAmount());
                    dto.setCurrency(sim.getCurrency());
                    dto.setInterestRate(sim.getInterestRate());
                    dto.setTerm(sim.getTerm());
                    dto.setMonthlyPayment(sim.getInstallment());
                    dto.setTotalPayment(sim.getTotalPayment());
                    dto.setApproved(sim.getAcceptance());
                    dto.setCreatedAt(sim.getSimulationDate());
                    dto.setClientId(client.getClientId());
                    return dto;
                })
                .toList();
    }

}
