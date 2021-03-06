package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

//@DataJpaTest
@SpringBootTest
@AutoConfigureTestDatabase(replace = NONE)
public class RepositoriesTest {

    @Autowired

    LoanRepository loanRepository;
    @Autowired
    AccountRepository accountRepository;

    @Autowired
    CardRepository cardRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Test
    public void existLoans(){
        List<Loan> loans = loanRepository.findAll();

        assertThat(loans,is(not(empty())));

    }

    @Test
    public void existPersonalLoan(){
        List<Loan> loans = loanRepository.findAll();

        assertThat(loans, hasItem(hasProperty("name", is("Personal"))));

    }

    @Test
    public void existAccounts() {
        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts,is(not(empty())));
    }

    @Test
    public void existAccountNumber() {
        List<Account> accounts = accountRepository.findAll();

        assertThat(accounts, hasItem(hasProperty("number", is("VIN001"))));
        assertThat(accounts, hasItem(hasProperty("number", is("VIN002"))));
    }

    @Test
    public void existCards() {
        List<Card> cards = cardRepository.findAll();

        assertThat(cards,is(not(empty())));
    }

    @Test
    public void existCardType() {
        List<Card> cards = cardRepository.findAll();

        assertThat(cards, hasItem(hasProperty("type", is(CardType.CREDIT))));
        assertThat(cards, hasItem(hasProperty("type", is(CardType.DEBIT))));
    }

    @Test
    public void existClients() {
        List<Card> cards = cardRepository.findAll();

        assertThat(cards,is(not(empty())));
    }

    @Test
    public void existClientName() {
        List<Client> clients = clientRepository.findAll();

        assertThat(clients, hasItem(hasProperty("firstName", is("Melba"))));
    }

    @Test
    public void existTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions,is(not(empty())));
    }

    @Test
    public void existTransactionType() {
        List<Transaction> transactions = transactionRepository.findAll();

        assertThat(transactions, hasItem(hasProperty("type", is(TransactionType.CREDIT))));
        assertThat(transactions, hasItem(hasProperty("type", is(TransactionType.DEBIT))));
    }
}