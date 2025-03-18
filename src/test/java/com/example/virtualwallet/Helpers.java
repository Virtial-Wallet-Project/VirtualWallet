package com.example.virtualwallet;

import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Helpers {

    public static User createMockUser() {
        User mockUser = new User();
        mockUser.setUserId(1);
        mockUser.setUsername("MockUsername");
        mockUser.setEmail("mock@user.com");
        mockUser.setPassword("password");
        mockUser.setBalance(300);
        mockUser.setPhoneNumber("2222222222");
        mockUser.setAdmin(false);
        mockUser.setBlocked(false);
        mockUser.setAccountVerified(true);
        return mockUser;
    }

    public static User createAnotherMockUser() {
        User mockUser = new User();
        mockUser.setUserId(2);
        mockUser.setUsername("MockUsername2");
        mockUser.setEmail("mock@user.com2");
        mockUser.setPassword("password2");
        mockUser.setBalance(300);
        mockUser.setPhoneNumber("2222222223");
        mockUser.setAdmin(false);
        mockUser.setBlocked(false);
        mockUser.setAccountVerified(true);
        return mockUser;
    }

    public static CreditCard createMockCard() {
        CreditCard card = new CreditCard();
        card.setCardId(1);
        card.setCardNumber("1111111111111111");
        card.setCreatedBy(createMockUser());
        card.setCheckNumber("111");
        card.setCardHolder("MOCK USER");
        card.setExpirationDate(LocalDate.now());
        return card;
    }
}
