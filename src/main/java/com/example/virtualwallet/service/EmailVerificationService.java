package com.example.virtualwallet.service;

public interface EmailVerificationService {
    void sendVerificationEmail(String email, String token);

    String loadEmailTemplate();
}
