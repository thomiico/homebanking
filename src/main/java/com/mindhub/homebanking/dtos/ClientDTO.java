package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ClientDTO {
    private Set<AccountDTO> accounts = new HashSet<>();

    private Set<ClientLoanDTO> loans = new HashSet<>();

    private Set<CardDTO> cards = new HashSet<>();
    private long id;
    private String firstName;
    private String lastName;
    private String email;

    private String password;
    private ClientType type;

    public ClientDTO(){}
    public ClientDTO(Client client) {

        this.id = client.getId();

        this.firstName = client.getFirstName();

        this.lastName = client.getLastName();

        this.email = client.getEmail();

        this.password = client.getPassword();

        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(Collectors.toSet());

        this.loans = client.getClientLoan().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toSet());

        this.cards = client.getCard().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());

        this.type = client.getType();
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }


    public String getLastName() {
        return lastName;
    }


    public String getEmail() {
        return email;
    }


    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() {
        return loans;
    }

    public Set<CardDTO> getCards() {
        return cards;
    }

    public String getPassword() {
        return password;
    }

    public ClientType getType() {
        return type;
    }
}
