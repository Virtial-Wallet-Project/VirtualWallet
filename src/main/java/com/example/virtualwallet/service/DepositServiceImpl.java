package com.example.virtualwallet.service;

import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.helpers.PermissionHelpers;
import com.example.virtualwallet.models.CardForDummyAPI;
import com.example.virtualwallet.models.CreditCard;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repositories.TransactionRepository;
import com.example.virtualwallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;


@Service
public class DepositServiceImpl implements DepositService {

    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private final TransactionRepository transactionRepository;

    private final String MONEY_TRANSFER_API_URL = "http://localhost:8081/api/transfer/withdraw";

    @Autowired
    public DepositServiceImpl(UserRepository userRepository, RestTemplate restTemplate, TransactionRepository transactionRepository) {
        this.userRepository = userRepository;
        this.restTemplate = restTemplate;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public String depositMoney(User user, CreditCard userCard, double amount) {
        PermissionHelpers.checkIfCreator(userCard, user);

        if (amount <= 0) {
            throw new InvalidOperationException("Amount should be greater than zero!");
        }

        CardForDummyAPI card = new CardForDummyAPI();
        card.setCardNumber(userCard.getCardNumber());
        card.setCheckNumber(userCard.getCheckNumber());

        ResponseEntity<String> response = restTemplate.postForEntity(MONEY_TRANSFER_API_URL, card, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
                user.setBalance(user.getBalance() + amount);
                userRepository.updateUser(user);

            Transaction transaction = new Transaction();
            transaction.setSender(user);
            transaction.setRecipient(user);
            transaction.setAmount(amount);
            transaction.setTransactionDate(LocalDateTime.now());

            transactionRepository.createTransaction(transaction);
                return "Deposit Successful! New Balance: " + user.getBalance();

        } else {
            return "Deposit Failed! Insufficient funds!";
        }
    }
}
