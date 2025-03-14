package com.example.virtualwallet.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class CardUpdateDto {

    private int id;

    @NotNull(message = "Card holder name is required")
    @Size(min = 2, max = 30, message = "Card holder name must be between 2 and 30 characters")
    private String cardHolder;

    @NotNull(message = "Check number is required")
    @Pattern(regexp = "\\d{3}", message = "Check number must be exactly 3 digits and contain only digits")
    private String checkNumber;

    public CardUpdateDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public String getCheckNumber() {
        return checkNumber;
    }

    public void setCheckNumber(String checkNumber) {
        this.checkNumber = checkNumber;
    }
}
