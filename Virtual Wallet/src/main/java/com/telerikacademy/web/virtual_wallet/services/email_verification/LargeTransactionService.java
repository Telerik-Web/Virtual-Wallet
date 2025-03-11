package com.telerikacademy.web.virtual_wallet.services.email_verification;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class LargeTransactionService {

    private final JavaMailSender mailSender;

    public LargeTransactionService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerificationEmail(String email, String token) {

        String subject = "Verify Transaction - Virtual Wallet";
        String verificationLink = "http://localhost:3308/auth/verify/transaction?token=" + token;

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

    public String loadEmailTemplate() {
        String filePath = "src/main/resources/templates/VerifyTransaction.html";
        try {
            return new String(Files.readAllBytes(Paths.get(filePath)));
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
