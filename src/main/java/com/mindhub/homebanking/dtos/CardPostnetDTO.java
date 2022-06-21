package com.mindhub.homebanking.dtos;

import com.mindhub.homebanking.models.Card;
import com.mindhub.homebanking.models.Transaction;

public class CardPostnetDTO {

    private long id;
    private String cardNumber;
    private int cvv;
    private Double amount;
    private String description;

    public CardPostnetDTO(){}

    public CardPostnetDTO(Card card, Transaction transaction){
        this.id = transaction.getId();
        this.cardNumber = card.getNumber();
        this.cvv = card.getCvv();
        this.amount = transaction.getAmount();
        this.description = transaction.getDescription();

    }

    public long getId() {
        return id;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public int getCvv() {
        return cvv;
    }

    public Double getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
