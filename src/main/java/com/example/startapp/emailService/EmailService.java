package com.example.startapp.emailService;

import com.example.startapp.dto.MailBody;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Properties;

@Service

public class EmailService {
    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendSimpleMessage(MailBody mailBody) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(mailBody.to());
        message.setFrom("nazrin.shikhalievaa@gmail.com");
        message.setSubject(mailBody.subject());
        message.setText(mailBody.text());

        javaMailSender.send(message);
    }
}
