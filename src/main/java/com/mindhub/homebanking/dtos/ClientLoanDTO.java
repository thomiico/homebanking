package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.models.Loan;

public class ClientLoanDTO {
        private long id;
        private int payments;
        private String name;
        private Double amount;
        private long loanId;

        public ClientLoanDTO(){}

        public ClientLoanDTO(ClientLoan clientLoan){
            this.id = clientLoan.getId();
            this.loanId = clientLoan.getLoan().getId();
            this.name = clientLoan.getLoan().getName();
            this.amount = clientLoan.getAmount();
            this.payments = clientLoan.getPayments();
        }

    public long getId() {
        return id;
    }

    public int getPayments() {
        return payments;
    }

    public String getName() {
        return name;
    }

    public Double getAmount() {
        return amount;
    }

    public long getLoan() {
        return loanId;
    }
}

