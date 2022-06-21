package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String number;
    private LocalDate creationDate;
    private double balance;
    private AccountType type;
    private boolean isEnable;

    // ManyToOne de CLIENT
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="client_id")
    private Client client;

    // OneToMany de transactions
    @OneToMany(mappedBy="account", fetch=FetchType.EAGER)
    private Set<Transaction> transactions  = new HashSet<>();

    public Account() {
    }

    public Account(String number, AccountType type, LocalDate creationDate, double balance, boolean isEnable, Client client) {
        this.number = number;
        this.type = type;
        this.creationDate = creationDate;
        this.balance = balance;
        this.isEnable = isEnable;
        this.client = client;
    }

    public long getId() {
        return id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Set<Transaction> getTransactions() {
        return transactions;
    }

    public void addTransactions(Transaction transaction) {
        transaction.setAccount(this);
        transactions.add(transaction);
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public AccountType getType() {
        return type;
    }

    public void setType(AccountType type) {
        this.type = type;
    }
}