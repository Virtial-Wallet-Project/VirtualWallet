package com.example.virtualwallet.models;

//This class is used to make a card model for the dummy restAPI in order to withdraw money

import com.fasterxml.jackson.annotation.JsonProperty;

public class CardForDummyAPI {

    @JsonProperty("cardNumber")
    private String cardNumber;

    @JsonProperty("checkNumber")
    private String checkNumber;

    public CardForDummyAPI() {
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
