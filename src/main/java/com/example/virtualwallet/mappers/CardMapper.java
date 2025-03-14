package com.example.virtualwallet.mappers;

import com.example.virtualwallet.DTOs.CardDto;
import com.example.virtualwallet.DTOs.CardUpdateDto;
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

    public CreditCard dtoToObjectForUpdate (CardUpdateDto cardDto, User user) {
        CreditCard card = cardService.getByUserId(user.getUserId());
        card.setCardNumber(cardDto.getCardNumber());
        card.setCardHolder(cardDto.getCardHolder());
        card.setCheckNumber(cardDto.getCheckNumber());
        return card;
    }

    public CardDto objectToDto (CreditCard card) {
        CardDto cardDto = new CardDto();
        cardDto.setCardNumber(card.getCardNumber());
        cardDto.setExpirationDate(card.getExpirationDate());
        cardDto.setCardHolder(card.getCardHolder());
        cardDto.setCheckNumber(card.getCheckNumber());
        return cardDto;
    }
}
