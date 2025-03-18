package com.example.virtualwallet.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class EmailVerificationServiceImpl implements EmailVerificationService {

    private final JavaMailSender mailSender;

    public EmailVerificationServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendVerificationEmail(String email, String token) {

        String subject = "Verify Your Email - Virtual Wallet";
        String verificationLink = "http://localhost:3308/auth/verify?token=" + token;
        String message = "Click the link below to verify your email: \n" + verificationLink;

        String htmlMessage = loadEmailTemplate();
        htmlMessage = htmlMessage.replace("{verificationLink}", verificationLink);
        htmlMessage = htmlMessage.replace("{YOUR-TOKEN}", token);

        String finalHtmlMessage = htmlMessage;

        MimeMessagePreparator msgPreparator = mimeMessage -> {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
            messageHelper.setFrom("no_reply@virtual-wallet.com");
            messageHelper.setTo(email);
            messageHelper.setSubject(subject);
            messageHelper.setText(finalHtmlMessage, true);
        };

        mailSender.send(msgPreparator);
    }

    @Override
    public String loadEmailTemplate() {
        String filePath = "src/main/resources/templates/verify-email-page.html";
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
