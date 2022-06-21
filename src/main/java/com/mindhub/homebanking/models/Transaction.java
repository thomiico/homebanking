package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private TransactionType type;
    private double amount;
    private String description;
    private LocalDate date;
    private double newBalance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="transactions_id")
    private Account account;

    public Transaction(){}

    public Transaction(TransactionType typeC, double amount, String description, LocalDate date1, double newBalance, Account accounts){
        this.type = typeC;
        this.amount = amount;
        this.description = description;
        this.date = date1;
        this.newBalance = newBalance;
        this.account = accounts;
    }

    public long getId() {
        return id;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(double newBalance) {
        this.newBalance = newBalance;
    }

}
