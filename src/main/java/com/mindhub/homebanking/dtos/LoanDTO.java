package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Loan;

import java.util.ArrayList;
import java.util.List;

public class LoanDTO {

    private long id;
    private String name;
    private Double maxAmount;
    private List<Integer> loanList = new ArrayList<>();
    private Double percent;

    public LoanDTO(){}

    public LoanDTO(Loan loan){
        this.id = loan.getId();
        this.name = loan.getName();
        this.maxAmount = loan.getMaxAmount();
        this.loanList = loan.getPayments();
        this.percent = loan.getPercent();
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public List<Integer> getLoanList() {
        return loanList;
    }

    public Double getPercent() {
        return percent;
    }
}
