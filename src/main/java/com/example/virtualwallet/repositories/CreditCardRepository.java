package com.example.virtualwallet.repositories;

import com.example.virtualwallet.models.CreditCard;

import java.util.List;

public interface CreditCardRepository {
    List<CreditCard> getAll();

    CreditCard getById(int id);

    CreditCard getByUserId(int userId);

    CreditCard getByCardNumber(String cardNumber);

    void createCard(CreditCard card);

    void updateCard(CreditCard card);

    void deleteCard(int id);
}
