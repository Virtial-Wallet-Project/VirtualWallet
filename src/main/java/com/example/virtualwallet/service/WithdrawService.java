package com.example.virtualwallet.service;

import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.User;

public interface WithdrawService {
    String depositMoney(User user, CreditCard card, double amount);
}
