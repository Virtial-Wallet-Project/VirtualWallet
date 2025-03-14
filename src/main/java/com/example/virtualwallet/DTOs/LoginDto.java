package com.example.virtualwallet.DTOs;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class LoginDto {

    @NotEmpty
    @Size(min = 2, max = 20, message = "Username must be between 2 and 20 symbols!")
    private String username;

    @NotEmpty
    @Size(min = 8, message = "Password must be above 8 symbols!")
    private String password;

    public LoginDto() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
