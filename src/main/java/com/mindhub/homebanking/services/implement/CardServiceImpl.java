package com.mindhub.homebanking.services.implement;

import com.mindhub.homebanking.dtos.CardDTO;
import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.repositories.CardRepository;
import com.mindhub.homebanking.services.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardRepository cardRepository;

    @Override
    public List<Card> getCards() {
        return cardRepository.findAll();
    }

    @Override
    public CardDTO getCardDto(long id) {
        return cardRepository.findById(id).map(CardDTO::new).orElse(null);
    }

    @Override
    public void saveCard(Card card) {
        cardRepository.save(card);
    }

    @Override
    public Card getCardById(long id) {
        return cardRepository.findById(id).orElse(null);
    }

    @Override
    public Card getCardByNumber(String number) {
        return cardRepository.findByNumber(number);
    }
}
