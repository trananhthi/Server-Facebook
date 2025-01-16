package com.example.trananhthi.service;

import jakarta.mail.MessagingException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;

public interface MyEmailService {

    @Async
    @Retryable(maxAttempts = 10)
    void sendSimpleMessage(String to, String subject, String text) throws MessagingException;
}
