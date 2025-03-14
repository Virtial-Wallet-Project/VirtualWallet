package com.example.virtualwallet.service;

import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;

import java.util.List;

public interface CreditCardService {
    List<CreditCard> getAll();

    CreditCard getById(int id);

    List<CreditCard> getByUserId(int id);

    CreditCard getByCardNumber(String cardNumber);

    void createCard(User user, CreditCard card);

    void updateCard(User user, CreditCard card);

    void deleteCard(User user, int cardId);
}
