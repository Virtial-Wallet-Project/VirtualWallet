package com.example.virtualwallet.models;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDateTime;

public class TransactionDto {

    @NotNull(message = "Sender is required")
    private User sender;

    @NotNull(message = "Recipient is required")
    private User recipient;

    @NotNull(message = "Amount is required")
    private double amount;

    @NotNull(message = "Transaction date is required")
    @PastOrPresent(message = "Transaction date must be in the past or present")
    private LocalDateTime transactionDate;


    public TransactionDto() {
    }


    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getRecipient() {
        return recipient;
    }

    public void setRecipient(User recipient) {
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

