package com.fof.server.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EmailService {

    private final static Logger LOGGER = LoggerFactory.getLogger(EmailService.class);
    private final JavaMailSender mailSender;

    @Async
    public void sendEmail(String to, String subject, String text) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setText(text, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("noreply@ventureverse.com");
            mailSender.send(mimeMessage);
        } catch (MessagingException Error) {
            LOGGER.error("Failed to send email", Error);
            throw new IllegalStateException("Failed to send email");
        }

    }

}
