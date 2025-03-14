package com.example.virtualwallet.service;

import com.example.virtualwallet.filtering.FilterTransactionOptions;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;

import java.util.List;

public interface TransactionService {
    List<Transaction> getAll(FilterTransactionOptions filterOptions, int page, int size, User user);

    Transaction getById(User user, int id);

    Transaction createTransaction(User sender, int recipientId, Transaction transaction);
}
