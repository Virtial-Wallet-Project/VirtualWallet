package com.example.virtualwallet.models;

import java.time.LocalDateTime;
import java.util.Optional;

public class FilterTransactionOptions {
    private Optional<Integer> userId;
    private Optional<Integer> senderId;
    private Optional<Integer> recipientId;
    private Optional<LocalDateTime> startDate;
    private Optional<LocalDateTime> endDate;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterTransactionOptions(Integer userId, Integer senderId, Integer recipientId,
                                    LocalDateTime startDate, LocalDateTime endDate,
                                    String sortBy, String sortOrder) {
        this.userId = Optional.ofNullable(userId);
        this.senderId = Optional.ofNullable(senderId);
        this.recipientId = Optional.ofNullable(recipientId);
        this.startDate = Optional.ofNullable(startDate);
        this.endDate = Optional.ofNullable(endDate);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<Integer> getUserId() {
        return userId;
    }

    public Optional<Integer> getSenderId() {
        return senderId;
    }

    public Optional<Integer> getRecipientId() {
        return recipientId;
    }

    public Optional<LocalDateTime> getStartDate() {
        return startDate;
    }

    public Optional<LocalDateTime> getEndDate() {
        return endDate;
    }

    public Optional<String> getSortBy() {
        return sortBy;
    }

    public Optional<String> getSortOrder() {
        return sortOrder;
    }
}
