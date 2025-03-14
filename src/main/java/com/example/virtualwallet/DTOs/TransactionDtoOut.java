package com.example.virtualwallet.DTOs;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public class TransactionDtoOut {

    @NotNull(message = "Sender is required")
    private UserDtoOutForTransactions sender;

    @NotNull(message = "Recipient is required")
    private UserDtoOutForTransactions recipient;

    @NotNull(message = "Amount is required")
    private double amount;

    @NotNull(message = "Transaction date is required")
    @PastOrPresent(message = "Transaction date must be in the past or present")
    private LocalDateTime transactionDate;


    public TransactionDtoOut() {
    }


    public UserDtoOutForTransactions getSender() {
        return sender;
    }

    public void setSender(UserDtoOutForTransactions sender) {
        this.sender = sender;
    }

    public UserDtoOutForTransactions getRecipient() {
        return recipient;
    }

    public void setRecipient(UserDtoOutForTransactions recipient) {
        this.recipient = recipient;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }
}

