package com.ra.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    @Value("${spring.mail.from}")
    private String emailFrom;

    public String sendEmail(String recipient, String subject, String content, MultipartFile[] files) throws MessagingException, UnsupportedEncodingException {
        // Send email
        log.info("Sending..");
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
        helper.setFrom(emailFrom, "RA");
        if (recipient.contains(",")) {
            helper.setTo(InternetAddress.parse(recipient));
        } else {
            helper.setTo(recipient);
        }

        helper.setSubject(subject);
        if (files != null && files.length > 0) {
            for (MultipartFile file : files) {
                helper.addAttachment(Objects.requireNonNull(file.getOriginalFilename()), file);
            }
        }
        helper.setText(content, true);
        javaMailSender.send(message);
        log.info("Email has been sent to {} ", recipient + " successfully");
        return "Email has been sent to " + recipient + " successfully";

    }
}
