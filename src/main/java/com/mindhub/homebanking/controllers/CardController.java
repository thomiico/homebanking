package com.mindhub.homebanking.controllers;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.*;
import com.mindhub.homebanking.services.CardService;
import com.mindhub.homebanking.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.mindhub.homebanking.Utils.Utils.randomNum;

@RestController
@RequestMapping("/api")
public class CardController {
    @Autowired
    private ClientService clientService;

    @Autowired
    private CardService cardService;

    @GetMapping("/cards")
    public List<Card> getCards() { // @JsonIgnore
        return cardService.getCards();
    }

    @GetMapping("/cards/{id}")
    public CardDTO getCard(@PathVariable long id){
        return cardService.getCardDto(id);
    }

    @PostMapping(path="/clients/current/cards")
    public ResponseEntity<Object> newCard(@RequestParam CardType cardType, @RequestParam ColorCard colorCard, Authentication authentication){
        Client client = clientService.getClientCurrent(authentication);
        if(client.getCard().stream().filter(card -> card.getType() == cardType && card.isEnable()).collect(Collectors.toList()).size() == 3){
            return new ResponseEntity<>("You have MAX cards available", HttpStatus.FORBIDDEN);
        }

        int cvv;
        cvv = randomNum(999, 100);

        String card1 = "4517";
        String card2 = "";
        String card3 = "";
        String card4 = "";
        String cardNumber;

        do {
            card2 = "";
            card3 = "";
            card4 = "";
            for (int i = 0; i < 12; i++) {
                switch (i) {

                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        card2 += randomNum(9, 0);
                        continue;

                    case 4:
                    case 5:
                    case 6:
                    case 7:
                        card3 += randomNum(9, 0);
                        continue;

                    case 8:
                    case 9:
                    case 10:
                    case 11:
                        card4 += randomNum(9, 0);
                        continue;

                }
            }
            cardNumber = card1+card2+card3+card4;
        }while(cardService.getCards().stream().map(card -> card.getNumber()).collect(Collectors.toList()).contains(cardNumber));

        Card cardSave = new Card(client, cardType, colorCard, cardNumber, cvv, LocalDateTime.now(), LocalDateTime.now().plusYears(5), true);
        cardService.saveCard(cardSave);
        System.out.println(cardNumber);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping("/clients/current/cards")
    public Set<CardDTO> getCards(Authentication authentication) {
        Client client = clientService.getClientCurrent(authentication);
        return client.getCard().stream().map(card -> new CardDTO(card)).collect(Collectors.toSet());
    }

    @PostMapping("/clients/current/cards/{id}")
    public ResponseEntity<Object> disabledCard(Authentication authentication, @PathVariable Long id){

        Client client = clientService.getClientCurrent(authentication);
        Card card = cardService.getCardById(id);

        if(card == null){
            return new ResponseEntity<>("Card not found", HttpStatus.FORBIDDEN);
        }

        if(!client.getCard().contains(card)){
            return new ResponseEntity<>("This card not own you", HttpStatus.FORBIDDEN);
        }

        if(!card.isEnable()){
            return new ResponseEntity<>("This Card is not enable!", HttpStatus.FORBIDDEN);
        }

        card.setEnable(false);
        cardService.saveCard(card);

        return new ResponseEntity<>("OK, Card Disabled!", HttpStatus.OK);
    }
}







