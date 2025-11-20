package com.inetum.apisimulationloans.repository;

import com.inetum.apisimulationloans.model.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, Long> {
    // Nuevo método para comprobar existencia por nombres
    boolean existsByFirstNameAndPaternalLastNameAndMaternalLastName(String firstName, String paternalLastName, String maternalLastName);

    // Nuevo método para obtener cliente por nombres
    Optional<Client> findByFirstNameAndPaternalLastNameAndMaternalLastName(String firstName, String paternalLastName, String maternalLastName);
}
