package com.project.doctorhub.sms.service;

import com.project.doctorhub.user.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class SmsServiceImpl implements SmsService {

    @Override
    public void sendSms(String phone, String message) {
        log.info("Sms send to {} with body {}", phone, message);
    }

    @Override
    public void sendSms(User user, String message) {
        log.info("Sms send to {} with body {}", user.getId(), message);
    }

    @Override
    public void sendAsyncSms(String phone, String message) {
        CompletableFuture.runAsync(() -> sendSms(phone, message));
    }

    @Override
    public void sendAsyncSms(User user, String message) {
        CompletableFuture.runAsync(() -> sendSms(user, message));
    }
}
