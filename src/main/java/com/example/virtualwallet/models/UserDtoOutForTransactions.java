package com.example.virtualwallet.models;

public class UserDtoOutForTransactions {

    private String username;

    public UserDtoOutForTransactions() {
    }

    public UserDtoOutForTransactions(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
