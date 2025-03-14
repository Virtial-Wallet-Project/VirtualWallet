package com.example.virtualwallet.repositories;

import com.example.virtualwallet.filtering.FilterTransactionOptions;
import com.example.virtualwallet.models.Transaction;

import java.util.List;

public interface TransactionRepository {

    List<Transaction> getAll(FilterTransactionOptions filterOptions, int page, int size);

    Transaction getById(int id);

    void createTransaction(Transaction transaction);

    void updateTransaction(Transaction transaction);

    void deleteTransaction(int id);
}
