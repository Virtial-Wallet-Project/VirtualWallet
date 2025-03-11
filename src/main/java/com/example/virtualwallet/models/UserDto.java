package com.example.virtualwallet.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserDto {

    @NotNull(message = "Username cannot be empty!")
    @Size(min = 2, max = 20, message = "Username should be between 2 and 20 symbols!")
    private String username;

    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    @NotNull(message = "Email should be valid an non-empty!")
    private String email;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[+\\-*&^%$#@!])[A-Za-z\\d+\\-*&^%$#@!]{8,}$",
            message = "Password must be at least 8 characters long, " +
                    "contain at least one capital letter, one digit, and one special symbol (+, -, *, &, ^, …).")
    @NotNull(message = "Password cannot be empty!")
    private String password;

    @NotNull(message = "Phone number can't be empty!")
    @Size(min = 10, max = 10, message = "Phone number length can't be less than 10 digits!")
    @Pattern(regexp = "^\\+?[1-9]\\d{0,2}[\\s.-]?[(]?[0-9]{1,4}[)]?[\\s.-]?\\d{1,4}[\\s.-]?\\d{1,4}[\\s.-]?\\d{1,9}$",
            flags = Pattern.Flag.CASE_INSENSITIVE)
    private String phoneNumber;

    public UserDto() {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
