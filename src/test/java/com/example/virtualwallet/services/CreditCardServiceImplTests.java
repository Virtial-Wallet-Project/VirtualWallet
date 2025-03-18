package com.example.virtualwallet.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.example.virtualwallet.exceptions.DuplicateEntityException;
import com.example.virtualwallet.exceptions.EntityNotFoundException;
import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repositories.CreditCardRepository;
import com.example.virtualwallet.service.CreditCardServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class CreditCardServiceImplTests {

    @Mock
    private CreditCardRepository creditCardRepository;

    @InjectMocks
    private CreditCardServiceImpl creditCardService;

    private User user;
    private CreditCard card;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUserId(1);
        user.setCards(new ArrayList<>());

        card = new CreditCard();
        card.setCardId(1);
        card.setCardNumber("1234-5678-9012-3456");
        card.setCreatedBy(user);
        card.setExpirationDate(LocalDate.of(2025, 12, 31));
        card.setCardHolder("John Doe");
        card.setCheckNumber("123");
    }

    @Test
    void testGetAll() {
        when(creditCardRepository.getAll()).thenReturn(Arrays.asList(card));
        List<CreditCard> cards = creditCardService.getAll();
        assertEquals(1, cards.size());
    }

    @Test
    void testGetById() {
        when(creditCardRepository.getById(1)).thenReturn(card);
        CreditCard foundCard = creditCardService.getById(1);
        assertEquals("1234-5678-9012-3456", foundCard.getCardNumber());
    }

    @Test
    void testGetByUserId() {
        when(creditCardRepository.getByUserId(1)).thenReturn(Arrays.asList(card));
        List<CreditCard> userCards = creditCardService.getByUserId(1);
        assertEquals(1, userCards.size());
    }

    @Test
    void testGetByCardNumber() {
        when(creditCardRepository.getByCardNumber("1234-5678-9012-3456")).thenReturn(card);
        CreditCard foundCard = creditCardService.getByCardNumber("1234-5678-9012-3456");
        assertEquals("1234-5678-9012-3456", foundCard.getCardNumber());
    }

    @Test
    void testCreateCard_Success() {
        when(creditCardRepository.getByUserId(1)).thenReturn(new ArrayList<>());
        when(creditCardRepository.getByCardNumber(card.getCardNumber())).thenThrow(new EntityNotFoundException());

        assertDoesNotThrow(() -> creditCardService.createCard(user, card));
        verify(creditCardRepository).createCard(card);
    }

    @Test
    void testCreateCard_DuplicateException() {
        when(creditCardRepository.getByCardNumber(card.getCardNumber())).thenReturn(card);

        assertThrows(DuplicateEntityException.class, () -> creditCardService.createCard(user, card));
    }

    @Test
    void testCreateCard_MaxCardsException() {
        when(creditCardRepository.getByUserId(1)).thenReturn(Arrays.asList(new CreditCard(), new CreditCard(), new CreditCard()));
        when(creditCardRepository.getByCardNumber(card.getCardNumber())).thenThrow(new EntityNotFoundException());

        assertThrows(InvalidOperationException.class, () -> creditCardService.createCard(user, card));
    }

    @Test
    void testUpdateCard_Success() {
        when(creditCardRepository.getByCardNumber(card.getCardNumber())).thenReturn(new CreditCard());

        assertDoesNotThrow(() -> creditCardService.updateCard(user, card));
        verify(creditCardRepository).updateCard(card);
    }

    @Test
    void testUpdateCard_NoChangesException() {
        when(creditCardRepository.getByCardNumber(card.getCardNumber())).thenReturn(card);

        assertThrows(InvalidOperationException.class, () -> creditCardService.updateCard(user, card));
    }

    @Test
    void testDeleteCard_Success() {
        when(creditCardRepository.getById(1)).thenReturn(card);

        assertDoesNotThrow(() -> creditCardService.deleteCard(user, 1));
        verify(creditCardRepository).deleteCard(1);
        assertFalse(user.getCards().contains(card));
    }
}
