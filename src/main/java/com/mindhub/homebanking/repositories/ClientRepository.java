package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource // crea los controladores para el repositorio (GET, POST, etc)
public interface ClientRepository extends JpaRepository<Client, Long> {

    Client findByEmail(String email);
}