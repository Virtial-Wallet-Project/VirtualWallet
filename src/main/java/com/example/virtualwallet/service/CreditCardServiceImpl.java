package com.example.virtualwallet.service;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.helpers.PermissionHelpers;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repositories.CreditCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;

    @Autowired
    public CreditCardServiceImpl(CreditCardRepository creditCardRepository) {
        this.creditCardRepository = creditCardRepository;
    }

    @Override
    public List<CreditCard> getAll() {
        return creditCardRepository.getAll();
    }

    @Override
    public CreditCard getById(int id) {
        return creditCardRepository.getById(id);
    }

    @Override
    public List<CreditCard> getByUserId(int id) {
        return creditCardRepository.getByUserId(id);
    }

    @Override
    public CreditCard getByCardNumber(String cardNumber) {
        return creditCardRepository.getByCardNumber(cardNumber);
    }

    @Override
    public void createCard(User user, CreditCard card) {

        boolean cardExists = true;

        try {
            creditCardRepository.getByCardNumber(card.getCardNumber());

        } catch (EntityNotFoundException e) {
            cardExists = false;
        }

        if (cardExists) {
            throw new DuplicateEntityException("Card", "number", card.getCardNumber());
        }

        List<CreditCard> userCards = creditCardRepository.getByUserId(user.getUserId());
        if (userCards.size() >= 3) {
            throw new InvalidOperationException("User has 3 cards and cannot add more!");
        }

        user.getCards().add(card);
        card.setCreatedBy(user);
        creditCardRepository.createCard(card);
    }

    @Override
    public void updateCard(User user, CreditCard card) {
        PermissionHelpers.checkIfCreator(card, user);

        CreditCard existingCard = creditCardRepository.getByCardNumber(card.getCardNumber());

        if (card.getCheckNumber().equals(existingCard.getCheckNumber()) &&
                card.getCardHolder().equals(existingCard.getCardHolder())) {

            throw new InvalidOperationException("Invalid operation! No changes were made!");
        }

        creditCardRepository.updateCard(card);
    }

    @Override
    public void deleteCard(User user, int cardId) {

        CreditCard card = creditCardRepository.getById(cardId);
        PermissionHelpers.checkIfCreator(card, user);
        creditCardRepository.deleteCard(cardId);
        user.getCards().remove(card);
    }
}
