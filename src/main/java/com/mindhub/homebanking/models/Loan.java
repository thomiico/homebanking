package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String name;
    private Double maxAmount;
    private Double percent;

    //OneToMany de Payments
    @ElementCollection
    @Column(name="payments")
    private List<Integer> payments = new ArrayList<>();

    // OneToMany de ClientLoan
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private List<ClientLoan> clientLoan = new ArrayList<>();

    public Loan(){}

    public Loan(String name, Double maxAmount, List<Integer> payments, Double percent){
        this.name = name;
        this.maxAmount = maxAmount;
        this.payments = payments;
        this.percent = percent;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getMaxAmount() {
        return maxAmount;
    }

    public void setMaxAmount(Double maxAmount) {
        this.maxAmount = maxAmount;
    }

    public List<Integer> getPayments() {
        return payments;
    }

    public void setPayments(List<Integer> payments) {
        this.payments = payments;
    }

    public List<ClientLoan> getClientLoan() {
        return clientLoan;
    }

    public void setClientLoan(List<ClientLoan> clientLoan) {
        this.clientLoan = clientLoan;
    }

    public List<Client> getClients(){
        return clientLoan.stream().map(client -> client.getClient()).collect(Collectors.toList());
    }

    public Double getPercent() {
        return percent;
    }

    public void setPercent(Double percent) {
        this.percent = percent;
    }
}
