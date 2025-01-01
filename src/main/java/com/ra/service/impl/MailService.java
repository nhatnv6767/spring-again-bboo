package com.ra.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailService {
    private final JavaMailSender javaMailSender;
    private final SpringTemplateEngine templateEngine;
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

    public void sendConfirmLink(String email, Long id, String secretCode) throws MessagingException, UnsupportedEncodingException {
        log.info("Sending confirmation link to {}", email);
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        Context context = new Context();
        String linkConfirm = String.format("http://192.168.1.202:8080/user/confirm/%s?secretCode=%s", id, secretCode);

        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);

        helper.setFrom(emailFrom, "RA-Academy");
        helper.setTo(email);
        helper.setSubject("Confirm your email address");

        String html = templateEngine.process("email/confirm-email", context);

        helper.setText(html, true);
        javaMailSender.send(message);

        log.info("Email has been sent to {} ", email + " successfully");

    }

    @KafkaListener(topics = "confirm-account-topic", groupId = "confirm-account-group")
    private void sendConfirmLinkByKafka(String message) throws MessagingException, UnsupportedEncodingException {
        log.info("Sending confirmation link by Kafka {}", message);

        String[] arr = message.split(",");
        String emailTo = arr[0];
        String id = arr[1];
        String secretCode = arr[2];
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());

        Context context = new Context();
        String linkConfirm = String.format("http://192.168.1.202:8080/user/confirm/%s?secretCode=%s", id, secretCode);

        Map<String, Object> properties = new HashMap<>();
        properties.put("linkConfirm", linkConfirm);
        context.setVariables(properties);

        helper.setFrom(emailFrom, "RA-Academy");
        helper.setTo(emailTo);
        helper.setSubject("Confirm your email address");

        String html = templateEngine.process("email/confirm-email", context);

        helper.setText(html, true);
        javaMailSender.send(mimeMessage);

        log.info("Link has been sent to {} ", emailTo + " successfully");

    }
}
