package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.AccountAndDateDTO;
import com.mindhub.homebanking.dtos.CardPostnetDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.models.Transaction;
import com.mindhub.homebanking.services.*;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.models.TransactionType.CREDIT;
import static com.mindhub.homebanking.models.TransactionType.DEBIT;

@RestController
@RequestMapping("/api")
public class TransactionController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @Autowired
    private TransactionService transactionService;
    @Autowired
    private PdfGenerator pdfGenerator;

    @GetMapping("/transactions")
    public List<Transaction> getTransactions(){
        return transactionService.getTransactions();
    }

    @CrossOrigin
    @Transactional
    @PostMapping("/transactions/postnet")
    public ResponseEntity<Object> newTransactionPostnet(@RequestBody CardPostnetDTO cardPostnetDTO){
        Card card = cardService.getCardByNumber(cardPostnetDTO.getCardNumber());
        String cardHolder = card.getCardholder();
        LocalDateTime localDateTime = card.getThruDate();
        Client client = card.getClient();
        List<Account> accounts = accountService.getAccountsByClient(client).stream().filter(acc -> acc.isEnable()).collect(Collectors.toList());
        //accounts.sort(Comparator.comparing(Account::getBalance));
        Comparator<Account> balanceComparator = Comparator.comparing(Account::getBalance);
        accounts.sort(balanceComparator.reversed());

        //accounts.stream().findFirst().orElse(null)
        // si pones una tarjeta que no está dentro del repositorio tira error 500

        if(accounts.get(0).getBalance() < cardPostnetDTO.getAmount()){
            return new ResponseEntity<>("This account does not have enough funds", HttpStatus.FORBIDDEN);
        }

        if(cardPostnetDTO.getCardNumber().isEmpty() || cardPostnetDTO.getAmount() <= 0 || cardPostnetDTO.getCvv() <= 0 || cardPostnetDTO.getDescription().isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }

        if(localDateTime.getYear() < LocalDateTime.now().getYear() || (localDateTime.getYear() == LocalDateTime.now().getYear() && localDateTime.getMonthValue() < LocalDateTime.now().getMonthValue())){
            return new ResponseEntity<>("The card is expired", HttpStatus.FORBIDDEN);
        }

        accounts.get(0).setBalance(accounts.get(0).getBalance() - cardPostnetDTO.getAmount());
        accountService.saveAccount(accounts.get(0));

        Transaction transaction = new Transaction(DEBIT, cardPostnetDTO.getAmount(), cardPostnetDTO.getDescription(), LocalDateTime.now(), accounts.get(0).getBalance(), accounts.get(0));
        transactionService.saveTransaction(transaction);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Transactional
    @PostMapping(path = "/transactions")
    public ResponseEntity<Object> newTransaction(

            Authentication authentication, @RequestParam double amount, @RequestParam String description,

            @RequestParam String accountOriginNumber, @RequestParam String accountDestinyNumber) {

        Client client = clientService.getClientCurrent(authentication);
        Account accountOrigin = accountService.getAccountByNumber(accountOriginNumber);
        Account accountDestiny = accountService.getAccountByNumber(accountDestinyNumber);

        if(description.isEmpty() || accountOriginNumber.isEmpty() || accountDestinyNumber.isEmpty()){
            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);
        }
        if(amount <= 0){
            return new ResponseEntity<>("Invalid Amount", HttpStatus.FORBIDDEN);
        }

        if(accountOrigin == accountDestiny){
            return new ResponseEntity<>("Origin account and Destiny account are the same", HttpStatus.FORBIDDEN);
        }

        if(accountOrigin == null){
            return new ResponseEntity<>("This account not exists", HttpStatus.FORBIDDEN);
        }

        if(!client.getAccounts().contains(accountOrigin)){
            return new ResponseEntity<>("This account does not belong to you", HttpStatus.FORBIDDEN);
        }

        if(accountDestiny == null){
            return new ResponseEntity<>("This account not exists", HttpStatus.FORBIDDEN);
        }

        if(accountOrigin.getBalance() < amount){
            return new ResponseEntity<>("Insufficient funds", HttpStatus.FORBIDDEN);
        }

        accountOrigin.setBalance(accountOrigin.getBalance() - amount);
        accountDestiny.setBalance(accountDestiny.getBalance() + amount);

        Transaction transactionDebit = new Transaction(DEBIT, amount, accountDestinyNumber + description, LocalDateTime.now(), accountDestiny.getBalance() , accountOrigin);
        Transaction transactionCredit = new Transaction(CREDIT, amount, accountOriginNumber + description, LocalDateTime.now(), accountOrigin.getBalance() , accountDestiny);

        transactionService.saveTransaction(transactionDebit);
        transactionService.saveTransaction(transactionCredit);
        accountService.saveAccount(accountOrigin);
        accountService.saveAccount(accountDestiny);

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/transactions/generate")
    public ResponseEntity<?> generatePdf(HttpServletResponse response, Authentication authentication, @RequestBody AccountAndDateDTO accountAndDateDTO) throws IOException, DocumentException, com.lowagie.text.DocumentException {
        if (accountAndDateDTO.getNumberAccount().isEmpty())
            return new ResponseEntity<>("Invalid number account", HttpStatus.FORBIDDEN);
        if (accountAndDateDTO.getSince().isAfter(LocalDateTime.now()))
            return new ResponseEntity<>("The date since is invalid", HttpStatus.FORBIDDEN);
        if (accountAndDateDTO.getUntil().isAfter(LocalDateTime.now()))
            return new ResponseEntity<>("The date until is invalid", HttpStatus.FORBIDDEN);


        Client client = clientService.getClientCurrent(authentication);
        Account account = accountService.getAccountByNumber(accountAndDateDTO.getNumberAccount());

        if (!client.getAccounts().contains(account))
            return new ResponseEntity<>("You are not the account owner.", HttpStatus.FORBIDDEN);


        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-mm-dd:hh:mm");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=dano-bank_" + accountAndDateDTO.getNumberAccount() + "-" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);
        pdfGenerator.export(response, authentication, accountAndDateDTO.getNumberAccount(), accountAndDateDTO.getSince(), accountAndDateDTO.getUntil());

        return new ResponseEntity<>("PDF Sended.", HttpStatus.ACCEPTED);
    }
}
