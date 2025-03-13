package com.example.virtualwallet.service;

import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;

public interface DepositService {
    String depositMoney(User user, CreditCard card, Double amount);
}
