package com.mindhub.homebanking.services;

import com.mindhub.homebanking.models.Transaction;

import java.util.List;

public interface TransactionService {

    void saveTransaction(Transaction transaction);

    List<Transaction> getTransactions();
}
