package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ClientLoanController {
    @Autowired
    private ClientLoanService clientLoanService;

    @GetMapping("/clientloans")
    public List<ClientLoanDTO> getClientLoans() {
        return clientLoanService.getClientLoans().stream().map(clientLoan -> new ClientLoanDTO(clientLoan)).collect(Collectors.toList());
    }

    @GetMapping("/clientloans/{id}")
    public ClientLoanDTO getClientLoan(@PathVariable Long id){
        return clientLoanService.getClientLoanDTO(id);
    }
}
