package com.example.virtualwallet.services;

import com.example.virtualwallet.service.EmailVerificationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmailVerificationServiceImplTests {

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private EmailVerificationServiceImpl emailService;

    private String email;
    private String token;

    @BeforeEach
    void setUp() {
        email = "test@example.com";
        token = "test-token";
    }

    @Test
    void sendVerificationEmail_Success() {
        emailService.sendVerificationEmail(email, token);
        verify(mailSender).send(any(MimeMessagePreparator.class));
    }

    @Test
    void loadEmailTemplate_Success() throws IOException {
        String expectedTemplate = "<html><body>Verify: {verificationLink}</body></html>";
        Files.write(Paths.get("src/main/resources/templates/verify-email-page.html"), expectedTemplate.getBytes());

        String result = emailService.loadEmailTemplate();
        assert result.contains("{verificationLink}");
    }
}