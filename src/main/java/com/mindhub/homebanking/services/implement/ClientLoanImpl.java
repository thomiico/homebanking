package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.ClientLoanDTO;
import com.mindhub.homebanking.models.ClientLoan;
import com.mindhub.homebanking.repositories.ClientLoanRepository;
import com.mindhub.homebanking.services.ClientLoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClientLoanImpl implements ClientLoanService {

    @Autowired
    private ClientLoanRepository clientLoanRepository;

    @Override
    public List<ClientLoan> getClientLoans() {
        return clientLoanRepository.findAll();
    }

    @Override
    public ClientLoanDTO getClientLoanDTO(long id) {
        return clientLoanRepository.findById(id).map(ClientLoanDTO::new).orElse(null);
    }

    @Override
    public void saveClientLoan(ClientLoan clientLoan) {
        clientLoanRepository.save(clientLoan);
    }
}
