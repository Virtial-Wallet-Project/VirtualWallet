package com.example.virtualwallet.helpers;

import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.TransactionDto;
import com.example.virtualwallet.service.TransactionService;
import com.example.virtualwallet.service.UserService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMapper {

    private final TransactionService transactionService;
    private final UserService userService;
    private final UserMapper userMapper;

    public TransactionMapper(TransactionService transactionService, UserService userService, UserMapper userMapper) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.userMapper = userMapper;
    }

    public Transaction fromDtoToObject (TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setSender(userService.getByUsername(transactionDto.getSender().getUsername()));
        transaction.setRecipient(userService.getByUsername(transactionDto.getRecipient().getUsername()));
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        return transaction;
    }

    public TransactionDto fromObjectToDto (Transaction transaction) {
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setSender(userMapper.userToDtoForTransactions(transaction.getSender()));
        transactionDto.setRecipient(userMapper.userToDtoForTransactions(transaction.getRecipient()));
        transactionDto.setAmount(transaction.getAmount());
        transactionDto.setTransactionDate(transaction.getTransactionDate());
        return transactionDto;
    }

    public List<TransactionDto> transactionsToDtoOut(List<Transaction> transactionList){
        List<TransactionDto> transactionDto = new ArrayList<>();
        for (Transaction transaction : transactionList){
            TransactionDto transactionDtoOut = new TransactionDto();
            transactionDtoOut.setSender(userMapper.userToDtoForTransactions(transaction.getSender()));
            transactionDtoOut.setRecipient(userMapper.userToDtoForTransactions(transaction.getRecipient()));
            transactionDtoOut.setAmount(transaction.getAmount());
            transactionDtoOut.setTransactionDate(transaction.getTransactionDate());

            transactionDto.add(transactionDtoOut);
        }

        return transactionDto;
    }
}
