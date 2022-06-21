package com.mindhub.homebanking;

import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.mindhub.homebanking.models.AccountType.CURRENT;
import static com.mindhub.homebanking.models.AccountType.SAVING;
import static com.mindhub.homebanking.models.ClientType.ADMIN;
import static com.mindhub.homebanking.models.ClientType.CLIENT;
import static com.mindhub.homebanking.models.ColorCard.*;
import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@SpringBootApplication
public class HomebankingApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	public static void main(String[] args) {
		SpringApplication.run(HomebankingApplication.class, args);
		System.out.println("Inicio!");
	}
	@Bean
	public CommandLineRunner initData(ClientRepository clientRepository, AccountRepository accountRepository, TransactionRepository transactionRepository,
									  LoanRepository loanRepository, ClientLoanRepository clientLoanRepository, CardRepository cardRepository) {
		return (args) -> {

			// save a couple of customers
			Client cliente1 = new Client("Melba", "Morel", "melba@mindhub.com", passwordEncoder.encode("bocamivida"), CLIENT);
			clientRepository.save(cliente1);
			Client cliente2 = new Client("Minerva", "McGonagall", "mmg@gryffindor.com", passwordEncoder.encode("harrypotter"), CLIENT);
			clientRepository.save(cliente2);
			Client cliente3 = new Client("Pedro", "Picapiedra", "picapiedra@mindhub.com", passwordEncoder.encode("melbamorel"), CLIENT);
			clientRepository.save(cliente3);
			Client admin = new Client("admin", "admin", "admin@admin.com", passwordEncoder.encode("admin1234"), ADMIN);
			clientRepository.save(admin);

			LocalDate timeNow = LocalDate.now();
			LocalDate timeTomorrow = timeNow.plusDays(1);

			Account cuenta1 = new Account("VIN001", SAVING, timeNow, 5000, true, cliente1);
			Account cuenta2 = new Account("VIN002", CURRENT,timeTomorrow, 7500, true, cliente1);
			accountRepository.save(cuenta1);
			accountRepository.save(cuenta2);

			Account cuenta3 = new Account("VIN003", SAVING, timeNow, 12000, true, cliente2);
			Account cuenta4 = new Account("VIN004", CURRENT, timeTomorrow, 2500, true, cliente2);
			accountRepository.save(cuenta3);
			accountRepository.save(cuenta4);

			Transaction transaction1 = new Transaction(CREDIT, 50000, "Mindhub", timeNow, cuenta1.getBalance(), cuenta1);
			Transaction transaction2 = new Transaction(DEBIT, 20000, "Mindhub2", timeNow, cuenta2.getBalance(), cuenta2);
			Transaction transaction3 = new Transaction(CREDIT, 70000, "Mindhub3", timeNow, cuenta3.getBalance(), cuenta3);
			Transaction transaction4 = new Transaction(DEBIT, 15000, "Mindhub4", timeNow, cuenta4.getBalance(), cuenta4);
			transactionRepository.save(transaction1);
			transactionRepository.save(transaction2);
			transactionRepository.save(transaction3);
			transactionRepository.save(transaction4);

			Transaction transaction5 = new Transaction(CREDIT, 70000, "Netflix", timeNow, cuenta1.getBalance(), cuenta1);
			Transaction transaction6 = new Transaction(DEBIT, 15000, "Mc Donalds", timeNow, cuenta2.getBalance(), cuenta2);
			Transaction transaction7 = new Transaction(CREDIT, 120000, "Hoyts Movie", timeNow, cuenta3.getBalance(), cuenta3);
			Transaction transaction8 = new Transaction(DEBIT, 45000, "Shop", timeNow, cuenta4.getBalance(), cuenta4);
			transactionRepository.save(transaction5);
			transactionRepository.save(transaction6);
			transactionRepository.save(transaction7);
			transactionRepository.save(transaction8);

			Loan loan1 = new Loan("Hipotecario", 500000.0, List.of(12, 24, 36, 48, 60), 12d);
			loanRepository.save(loan1);

			Loan loan2 = new Loan("Personal", 100000.0, List.of(6, 12, 24), 15d);
			loanRepository.save(loan2);

			Loan loan3 = new Loan("Automotriz", 300000.0, List.of(6, 12, 24, 36), 20d);
			loanRepository.save(loan3);

			ClientLoan clientLoan1 = new ClientLoan(400000.0, 60, cliente1, loan1);
			clientLoanRepository.save(clientLoan1);

			ClientLoan clientLoan2 = new ClientLoan(50000.0, 12, cliente1, loan2);
			clientLoanRepository.save(clientLoan2);

			ClientLoan clientLoan3 = new ClientLoan(100000.0, 24, cliente2, loan2);
			clientLoanRepository.save(clientLoan3);

			ClientLoan clientLoan4 = new ClientLoan(200000.0, 36, cliente2, loan3);
			clientLoanRepository.save(clientLoan4);

			Card card1 = new Card(cliente1, CardType.DEBIT, GOLD, "2424848472721010",  288, LocalDateTime.now(), LocalDateTime.now().plusYears(5), true);
			cardRepository.save(card1);

			Card card2 = new Card(cliente1, CardType.CREDIT, TITANIUM, "1616000024548312",  803, LocalDateTime.now(), LocalDateTime.now().plusYears(5), true);
			cardRepository.save(card2);

			Card card3 = new Card(cliente2, CardType.CREDIT, SILVER, "4848512706063184",  155, LocalDateTime.now(), LocalDateTime.now().plusYears(5), true);
			cardRepository.save(card3);

			Card card4 = new Card(cliente1, CardType.DEBIT, SILVER, "5454668820301547",  588, LocalDateTime.now(), LocalDateTime.now().plusYears(4), true);
			cardRepository.save(card4);

			Card card5 = new Card(cliente1, CardType.DEBIT, TITANIUM, "0303040405050606",  234, LocalDateTime.now(), LocalDateTime.now().plusYears(3), true);
			cardRepository.save(card5);
		};
	}
}
