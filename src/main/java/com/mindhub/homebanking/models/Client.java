package com.mindhub.homebanking.models;

import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;

@Entity // Nos crea una tabla en la base de datos
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    private ClientType type;
    // OneToMany de Account
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    // OneToMany de Card
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<Card> card = new HashSet<>();

    // OneToMany de ClientLoan
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoan = new HashSet<>();

    // Sobrecarga de metodos
    public Client() { }

    public Client(String firstName, String lastName, String email, String password, ClientType type) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.type = type;
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public Set<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(Set<Account> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Account account) {
        account.setClient(this);
        accounts.add(account);
    }

    public Set<ClientLoan> getClientLoan() {
        return clientLoan;
    }

    public void setClientLoan(Set<ClientLoan> clientLoan) {
        this.clientLoan = clientLoan;
    }

    public Set<Loan> getLoans(){
        return clientLoan.stream().map(loans -> loans.getLoan()).collect(Collectors.toSet());
    }

    public Set<Card> getCard() {
        return card;
    }

    public void setCard(Set<Card> card) {
        this.card = card;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ClientType getType() {
        return type;
    }

    public void setType(ClientType type) {
        this.type = type;
    }
}
