package com.example.virtualwallet.helpers;

import com.example.virtualwallet.models.Transaction;
import com.example.virtualwallet.models.TransactionDto;
import com.example.virtualwallet.service.TransactionService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class TransactionMapper {

    private final TransactionService transactionService;

    public TransactionMapper(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    public Transaction fromDtoToObject (TransactionDto transactionDto) {
        Transaction transaction = new Transaction();
        transaction.setSender(transactionDto.getSender());
        transaction.setRecipient(transactionDto.getRecipient());
        transaction.setAmount(transactionDto.getAmount());
        transaction.setTransactionDate(transactionDto.getTransactionDate());
        return transaction;
    }

    public List<TransactionDto> transactionsToDtoOut(List<Transaction> transactionList){
        List<TransactionDto> transactionDto = new ArrayList<>();
        for (Transaction transaction : transactionList){
            TransactionDto transactionDtoOut = new TransactionDto();
            transactionDtoOut.setSender(transaction.getSender());
            transactionDtoOut.setRecipient(transaction.getRecipient());
            transactionDtoOut.setAmount(transaction.getAmount());
            transactionDtoOut.setTransactionDate(transaction.getTransactionDate());

            transactionDto.add(transactionDtoOut);
        }

        return transactionDto;
    }
}
