package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.LoanApplicationDTO;
import com.mindhub.homebanking.dtos.LoanDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;


@RestController
@RequestMapping("/api")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private ClientLoanService clientLoanService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private TransactionService transactionService;

    @Transactional
    @PostMapping("/loans")
    public ResponseEntity<Object> newLoan(

            Authentication authentication, @RequestBody LoanApplicationDTO loanApplicationDTO) {

        Client client = clientService.getClientCurrent(authentication);
        Loan loan = loanService.getLoanById(loanApplicationDTO.getLoanID());
        Account accountDestiny = accountService.getAccountByNumber(loanApplicationDTO.getAccountNumber());

        if( loanApplicationDTO.getAmount() <= 0 || loanApplicationDTO.getPayments() <= 0){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(loan == null){
            return new ResponseEntity<>("This loan not exists", HttpStatus.FORBIDDEN);
        }

        if(client.getLoans().stream().filter(clientLoanDTO -> clientLoanDTO.getId() == loan.getId()).collect(Collectors.toList()).size() > 0){
            return new ResponseEntity<>("You already have this loan", HttpStatus.FORBIDDEN);
        }

        if(loan.getMaxAmount() < loanApplicationDTO.getPayments()){
            return new ResponseEntity<>("You exceeded the maximum allowed", HttpStatus.FORBIDDEN);
        }

        if(!loan.getPayments().contains(loanApplicationDTO.getPayments())){
            return new ResponseEntity<>("Number of payments not allowed", HttpStatus.FORBIDDEN);
        }

        if(accountDestiny == null){
            return new ResponseEntity<>("This account not exists", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().stream().map(Account::getNumber).collect(Collectors.toList()).contains(loanApplicationDTO.getAccountNumber())){
            return new ResponseEntity<>("This account does not belong to you", HttpStatus.FORBIDDEN);
        }

        Double amountInterest = loanApplicationDTO.getAmount() + (loanApplicationDTO.getAmount() * (loan.getPercent() * 0.01));
        Double balance = accountService.getAccountByNumber(loanApplicationDTO.getAccountNumber()).getBalance();

        accountDestiny.setBalance(accountDestiny.getBalance() + loanApplicationDTO.getAmount());
        Transaction transaction = new Transaction(CREDIT, loanApplicationDTO.getAmount(), loan.getName() +" loan approved", LocalDate.now(), balance, accountDestiny);
        ClientLoan clientLoan = new ClientLoan(amountInterest, loanApplicationDTO.getPayments(), client, loan);

        accountService.saveAccount(accountDestiny);
        transactionService.saveTransaction(transaction);
        clientLoanService.saveClientLoan(clientLoan);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/loan")
    public ResponseEntity<Object> createLoan(Authentication authentication, @RequestBody LoanDTO loanDTO){
        Client client = clientService.getClientCurrent(authentication);

        if(loanService.getLoans().stream().map(loandto -> new LoanDTO(loandto)).collect(Collectors.toList()).contains(loanDTO.getName())){
            return new ResponseEntity<>("This loan already exists!", HttpStatus.FORBIDDEN);
        }

        if(!client.getType().equals(ClientType.ADMIN)){
            return new ResponseEntity<>("You don't have access for create new loan!", HttpStatus.FORBIDDEN);
        }

        if(loanDTO.getName().isEmpty() || loanDTO.getMaxAmount() <= 0 || loanDTO.getPercent() <= 0.0){
            return new ResponseEntity<>("Missing Data", HttpStatus.FORBIDDEN);
        }

        Loan loan = new Loan(loanDTO.getName(), loanDTO.getMaxAmount(), loanDTO.getLoanList(), loanDTO.getPercent());
        loanService.saveLoan(loan);
        return new ResponseEntity<>(HttpStatus.CREATED);

        //{
        //    "name": "cuscus",
        //        "maxAmount": 1000000,
        //        "payments": [3,6,9,12],
        //    "interest": 5
        //}
    }

    @GetMapping("/loans")
    public List<LoanDTO> getLoans(){
        return loanService.getLoans().stream().map(loan -> new LoanDTO(loan)).collect(Collectors.toList());
    }

}
