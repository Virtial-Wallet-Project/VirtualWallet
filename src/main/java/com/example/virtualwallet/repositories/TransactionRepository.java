package com.example.virtualwallet.repositories;

import com.example.virtualwallet.models.Transaction;

import java.util.List;

public interface TransactionRepository {
    List<Transaction> getAll();

    Transaction getById(int id);

    List<Transaction> getBySenderId(int senderId);

    List<Transaction> getByRecipientId(int recipientId);

    void createTransaction(Transaction transaction);

    void updateTransaction(Transaction transaction);

    void deleteTransaction(int id);
}
