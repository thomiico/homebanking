package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.ClientLoan;

import java.util.List;

public interface ClientLoanService {

    List<ClientLoan> getClientLoans();

    ClientLoanDTO getClientLoanDTO(long id);

    void saveClientLoan(ClientLoan clientLoan);
}
