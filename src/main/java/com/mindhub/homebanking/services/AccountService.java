package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;

import java.util.List;

public interface AccountService {


    List<Account> getAccounts();

    Account getAccount(Long id);

    void saveAccount(Account account);

    Account getAccountByNumber(String number);
    List<Account> getAccountsByClient(Client client);

}
