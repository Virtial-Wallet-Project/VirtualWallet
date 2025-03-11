package com.example.virtualwallet.helpers;

import com.example.virtualwallet.models.CardDto;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.service.CreditCardService;
import org.springframework.stereotype.Component;

@Component
public class CardMapper {

    private final CreditCardService cardService;

    public CardMapper(CreditCardService cardService) {
        this.cardService = cardService;
    }

    public CreditCard dtoToObject (CardDto cardDto) {
        CreditCard card = new CreditCard();
        card.setCardNumber(cardDto.getCardNumber());
        card.setCheckNumber(cardDto.getCheckNumber());
        card.setExpirationDate(cardDto.getExpirationDate());
        card.setCardHolder(cardDto.getCardHolder());
        return card;
    }

    public CreditCard dtoToObjectForUpdate (CardDto cardDto, User user) {
        CreditCard card = cardService.getByUserId(user.getUserId());
        card.setCardNumber(cardDto.getCardNumber());
        card.setCardHolder(cardDto.getCardHolder());
        card.setCheckNumber(cardDto.getCheckNumber());
        return card;
    }
}
