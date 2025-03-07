package com.example.virtualwallet.models;

//This class is used to make a card model for the dummy restAPI in order to withdraw money

import com.fasterxml.jackson.annotation.JsonProperty;

public class Card {

    @JsonProperty("cardNumber")
    private String cardNumber;

    @JsonProperty("checkNumber")
    private String checkNumber;

    public Card() {
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }
}
