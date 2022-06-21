package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.ClientDTO;
import com.mindhub.homebanking.models.Account;
import com.mindhub.homebanking.models.Client;
import com.mindhub.homebanking.services.AccountService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import static com.mindhub.homebanking.Utils.Utils.numberAccount;
import static com.mindhub.homebanking.models.AccountType.SAVING;
import static com.mindhub.homebanking.models.ClientType.CLIENT;

@RestController
@RequestMapping("/api")
public class ClientController {

    @Autowired
    private ClientService clientService;

    @Autowired
    private AccountService accountService;

    // @Autowired: Inyeccion de dependencia - crea una instancia de lo que quiero utilizar en el lugar donde lo quiero utilizar
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/clients")
    public List<ClientDTO> getClientsDTO() { // @JsonIgnore
        return clientService.getClientsDto();
    }

    @GetMapping("/clients/{id}")
    public ClientDTO getClient(@PathVariable Long id) {

        return clientService.getClientDTO(id);

    }


    @PostMapping(path = "/clients")
    public ResponseEntity<Object> register(

            @RequestParam String firstName, @RequestParam String lastName,

            @RequestParam String email, @RequestParam String password) {


        if (firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() || password.isEmpty()) {

            return new ResponseEntity<>("Missing data", HttpStatus.FORBIDDEN);

        }


        if (clientService.getClientByEmail(email) != null) {

            return new ResponseEntity<>("Name already in use", HttpStatus.FORBIDDEN);

        }

        Client clientNew = new Client(firstName, lastName, email, passwordEncoder.encode(password), CLIENT);
        clientService.saveClient(clientNew);

        String accountNum;
        accountNum = "VIN" + numberAccount(accountService.getAccounts());

        LocalDate timeNow = LocalDate.now();
        accountService.saveAccount(new Account(accountNum, SAVING, timeNow, 0, true, clientNew));
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current")
    public ClientDTO getCurrentClientDTO(Authentication authentication) {
        Client client = clientService.getClientCurrent(authentication);
        ClientDTO clientDTO = new ClientDTO(client);
        return clientDTO;
    }
}
