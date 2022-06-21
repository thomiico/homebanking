package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    AccountRepository accountRepository;

    @Override
    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account getAccount(Long id) {
        return accountRepository.findById(id).orElse(null);
    }

    @Override
    public void saveAccount(Account account) {
        accountRepository.save(account);
    }

    @Override
    public Account getAccountByNumber(String number){
        return accountRepository.findByNumber(number);
    }

    @Override
    public List<Account> getAccountsByClient(Client client){
        return accountRepository.findByClient(client);
    }
}
