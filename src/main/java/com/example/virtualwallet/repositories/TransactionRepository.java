package com.example.virtualwallet.repositories;

import com.example.virtualwallet.models.FilterTransactionOptions;
import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.User;

import java.util.List;

public interface TransactionRepository {

    List<Transaction> getAll(FilterTransactionOptions filterOptions, int page, int size);

    List<Transaction> getAllTransactionsForLoggedUser (FilterTransactionOptions filterOptions, int page, int size, User user);

    Transaction getById(int id);

    List<Transaction> getBySenderId(int senderId);

    List<Transaction> getByRecipientId(int recipientId);

    void createTransaction(Transaction transaction);

    void updateTransaction(Transaction transaction);

    void deleteTransaction(int id);
}
