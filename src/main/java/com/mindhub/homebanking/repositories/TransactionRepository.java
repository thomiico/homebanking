package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource // crea los controladores para el repositorio (GET, POST, etc)
public interface TransactionRepository extends JpaRepository <Transaction, Long> {
}
