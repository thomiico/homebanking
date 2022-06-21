package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource // crea los controladores para el repositorio (GET, POST, etc)
public interface AccountRepository extends JpaRepository<Account, Long> {
        Account findByNumber(String account);
        List<Account> findByClient(Client client);

}