package com.mindhub.homebanking.services;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;

import java.util.List;

public interface CardService {

    List<Card> getCards();

    CardDTO getCardDto(long id);

    void saveCard(Card card);

    Card getCardById(long id);

    Card getCardByNumber(String number);
}
