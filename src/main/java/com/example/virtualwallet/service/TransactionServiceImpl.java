package com.example.virtualwallet.service;

import com.example.virtualwallet.helpers.PermissionHelpers;
import com.example.virtualwallet.models.FilterTransactionOptions;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;
import com.example.virtualwallet.repositories.TransactionRepository;
import com.example.virtualwallet.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Transaction> getAll(FilterTransactionOptions filterOptions, int page, int size, User user) {
        PermissionHelpers.checkIfAdmin(user);
        return transactionRepository.getAll(filterOptions, page, size);
    }

    @Override
    public List<Transaction> getAllTransactionsForLoggedUser(FilterTransactionOptions filterOptions, int page, int size, User user) {
        return transactionRepository.getAllTransactionsForLoggedUser(filterOptions, page, size, user);
    }

    @Override
    public Transaction getById(User user, int id) {
        PermissionHelpers.checkIfAdmin(user);
        return transactionRepository.getById(id);
    }

    @Override
    public Transaction createTransaction(User sender, int recipientId, double amount, Transaction transaction) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero!");
        }

        User recipient = userRepository.getById(recipientId);

        if (sender.equals(recipient)) {
            throw new IllegalArgumentException("You cannot send money to yourself!");
        }

        transactionRepository.createTransaction(transaction);
        return transaction;
    }

}
