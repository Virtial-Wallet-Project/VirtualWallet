package com.example.virtualwallet.mappers;

import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.DTOs.TransactionDto;
import com.example.virtualwallet.DTOs.TransactionDtoOut;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMapper {

    private final UserMapper userMapper;

    public TransactionMapper(UserMapper userMapper) {
        this.userMapper = userMapper;
    }

    public Transaction fromDtoToObject (TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        return transaction;
    }

    public TransactionDtoOut fromObjectToDto (Transaction transaction) {
        TransactionDtoOut transactionDto = new TransactionDtoOut();
        transactionDto.setSender(userMapper.userToDtoForTransactions(transaction.getSender()));
        transactionDto.setRecipient(userMapper.userToDtoForTransactions(transaction.getRecipient()));
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setTransactionDate(transaction.getTransactionDate());
        return transactionDto;
    }

    public List<TransactionDtoOut> transactionsToDtoOut(List<Transaction> transactionList){
        List<TransactionDtoOut> transactionDto = new ArrayList<>();
        for (Transaction transaction : transactionList){
            TransactionDtoOut transactionDtoOut = new TransactionDtoOut();
            transactionDtoOut.setSender(userMapper.userToDtoForTransactions(transaction.getSender()));
            transactionDtoOut.setRecipient(userMapper.userToDtoForTransactions(transaction.getRecipient()));
            transactionDtoOut.setAmount(transaction.getAmount());
            transactionDtoOut.setTransactionDate(transaction.getTransactionDate());

            transactionDto.add(transactionDtoOut);
        }

        return transactionDto;
    }
}
