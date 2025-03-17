package com.example.virtualwallet.service;

import com.example.virtualwallet.exceptions.InvalidOperationException;
import com.example.virtualwallet.helpers.PermissionHelpers;
import com.example.virtualwallet.filtering.FilterTransactionOptions;
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
        return transactionRepository.getAll(filterOptions, page, size);
    }

    @Override
    public Transaction getById(User user, int id) {
        PermissionHelpers.checkIfAdmin(user);
        return transactionRepository.getById(id);
    }

    @Override
    public Transaction createTransaction(User sender, int recipientId, Transaction transaction) {

        PermissionHelpers.checkIfVerified(sender);
        PermissionHelpers.checkIfBlocked(sender);

        if (transaction.getAmount() <= 0) {
            throw new InvalidOperationException("Amount must be greater than zero!");
        }

        User recipient = userRepository.getById(recipientId);

        if (sender.equals(recipient)) {
            throw new InvalidOperationException("You cannot send money to yourself!");
        }

        if (sender.getBalance() < transaction.getAmount()) {
            throw new InvalidOperationException("Invalid operation! Insufficient funds!");
        }

        transactionRepository.createTransaction(transaction);
        sender.setBalance(sender.getBalance() - transaction.getAmount());
        recipient.setBalance(recipient.getBalance() + transaction.getAmount());
        userRepository.updateUser(sender);
        userRepository.updateUser(recipient);
        return transaction;
    }
}
