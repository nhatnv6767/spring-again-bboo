package com.ra.controller;

import com.ra.dto.response.ResponseData;
import com.ra.service.impl.MailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@Slf4j
@RequestMapping("/common")
@RequiredArgsConstructor
public class CommonController {
    private final MailService mailService;

    @PostMapping("/send-email")
    public ResponseData<String> sendEmail(
            @RequestParam String recipients,
            @RequestParam String subject,
            @RequestParam String content,
            @RequestParam(required = false) MultipartFile[] files) {
        try {
            return new ResponseData<>(HttpStatus.ACCEPTED.value(), mailService.sendEmail(recipients, subject, content, files));
        } catch (Exception e) {
            log.error("Error sending email: {}", e.getMessage(), e.getCause());
            return new ResponseData<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error sending email: " + e.getMessage());
        }


    }

}
