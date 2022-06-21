package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.AccountType;
import com.mindhub.homebanking.models.Transaction;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class AccountDTO {

    Set<TransactionDTO> transactions = new HashSet<>();;
    private String number;
    private LocalDate creationDate;
    private double balance;
    private long id;
    private AccountType type;

    private boolean isEnable;

    public AccountDTO(){}

    public AccountDTO(Account accounts) {

        this.id = accounts.getId();

        this.number = accounts.getNumber();

        this.creationDate = accounts.getCreationDate();

        this.balance = accounts.getBalance();

        this.type = accounts.getType();

        this.isEnable = accounts.isEnable();

        this.transactions = accounts.getTransactions().stream().map(TransactionDTO::new).collect(Collectors.toSet());

    }

    public long getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public double getBalance() {
        return balance;
    }

    public Set<TransactionDTO> getTransactions() {
        return transactions;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public AccountType getType() {
        return type;
    }
}
