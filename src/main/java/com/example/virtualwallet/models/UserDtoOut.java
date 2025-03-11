package com.example.virtualwallet.models;

public class UserDtoOut {

    private String username;

    private String email;

    public UserDtoOut() {
    }

    public UserDtoOut(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
