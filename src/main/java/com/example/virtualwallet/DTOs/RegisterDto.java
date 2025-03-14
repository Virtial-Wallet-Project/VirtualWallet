package com.example.virtualwallet.DTOs;

import jakarta.validation.constraints.NotEmpty;

public class RegisterDto extends LoginDto {

    @NotEmpty
    private String passwordConfirm;

    @NotEmpty
    private String email;

    @NotEmpty
    private String phoneNumber;

    public RegisterDto() {
    }

    public String getPasswordConfirm() {
        return passwordConfirm;
    }

    public void setPasswordConfirm(String passwordConfirm) {
        this.passwordConfirm = passwordConfirm;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
