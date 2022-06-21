package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Transaction;

public class LoanApplicationDTO {

    private long loanID;
    private Double amount;
    private int payments;
    private String accountNumber;

    public LoanApplicationDTO(){}

    public LoanApplicationDTO(ClientLoan loan, Transaction transaction){
        this.loanID = loan.getId();
        this.amount = loan.getAmount();
        this.payments = loan.getPayments();
        this.accountNumber = transaction.getAccount().getNumber();
    }

    public long getLoanID() {
        return loanID;
    }

    public Double getAmount() {
        return amount;
    }

    public int getPayments() {
        return payments;
    }

    public String getAccountNumber() {
        return accountNumber;
    }
}
