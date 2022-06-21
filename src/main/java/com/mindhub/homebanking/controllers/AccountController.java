package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.repositories.AccountRepository;
import com.mindhub.homebanking.repositories.ClientRepository;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.Utils.Utils.numberAccount;

@RestController
@RequestMapping("/api")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @GetMapping("/accounts")
    public List<Account> getAccounts() { // @JsonIgnore
        return accountService.getAccounts();
    }

    @GetMapping("/accounts/{id}")
    public AccountDTO getAccount(@PathVariable Long id, Authentication authentication){
        Client client = clientService.getClientCurrent(authentication);
        Account account = accountService.getAccount(id);
        if(!client.getAccounts().contains(account)){
            return null;
        }
        return new AccountDTO(account);
    }

    @PostMapping(path="/clients/current/accounts")
    public ResponseEntity<Object> newAccount(Authentication authentication, @RequestParam AccountType type){
        Client client = clientService.getClientCurrent(authentication);
        if(client.getAccounts().size() >= 3){ // client.getAccounts().stream().count() >= 3
            return new ResponseEntity<>("You have MAX accounts!", HttpStatus.FORBIDDEN);
        }

        String accountNum;
        accountNum = "VIN" + numberAccount(accountService.getAccounts());

        LocalDate timeNow = LocalDate.now();
        accountService.saveAccount(new Account(accountNum, type, timeNow, 0, true, client));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/accounts")
    public List<AccountDTO> getCurrentAccounts(Authentication authentication){
        Client client = clientService.getClientCurrent(authentication);
        return client.getAccounts().stream().map(account -> new AccountDTO(account)).collect(Collectors.toList());
    }

    @PostMapping("/clients/current/accounts/{id}")
    public ResponseEntity<Object> disabledAccount(Authentication authentication, @PathVariable Long id){
        Client client = clientService.getClientCurrent(authentication);
        Account account = accountService.getAccount(id);

        if(account.getTransactions().contains("loan")){
            return new ResponseEntity<>("You cannot delete an account with an active loan", HttpStatus.FORBIDDEN);
        }

        if(account == null){
            return new ResponseEntity<>("Account not found", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().stream().map(Account::getNumber).collect(Collectors.toList()).contains(account)){
            return new ResponseEntity<>("This account not own you", HttpStatus.FORBIDDEN);
        }

        if(!account.isEnable()) {
            return new ResponseEntity<>("This account not enabled!", HttpStatus.FORBIDDEN);
        }

        account.setEnable(false);
        accountService.saveAccount(account);
        return new ResponseEntity<>("OK, Account disabled!", HttpStatus.OK);
    }

}