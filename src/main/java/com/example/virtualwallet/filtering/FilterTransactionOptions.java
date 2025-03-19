package com.example.virtualwallet.filtering;

import java.time.LocalDateTime;
import java.util.Optional;

public class FilterTransactionOptions {
    private Optional<String> userId;
    private Optional<String> sender;
    private Optional<String> recipient;
    private Optional<LocalDateTime> startDate;
    private Optional<LocalDateTime> endDate;
    private Optional<String> sortBy;
    private Optional<String> sortOrder;

    public FilterTransactionOptions(String userId, String sender, String recipient,
                                    LocalDateTime startDate, LocalDateTime endDate,
                                    String sortBy, String sortOrder) {
        this.userId = Optional.ofNullable(userId);
        this.sender = Optional.ofNullable(sender);
        this.recipient = Optional.ofNullable(recipient);
        this.startDate = Optional.ofNullable(startDate);
        this.endDate = Optional.ofNullable(endDate);
        this.sortBy = Optional.ofNullable(sortBy);
        this.sortOrder = Optional.ofNullable(sortOrder);
    }

    public Optional<String> getUserId() {
        return userId;
    }

    public void setUserId(Optional<String> userId) {
        this.userId = userId;
    }

    public Optional<String> getSender() {
        return sender;
    }

    public Optional<String> getRecipient() {
        return recipient;
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
