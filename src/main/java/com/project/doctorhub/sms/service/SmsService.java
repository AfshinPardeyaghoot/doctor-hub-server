package com.project.doctorhub.sms.service;

import com.project.doctorhub.user.model.User;

public interface SmsService {


    void sendSms(String phone, String message);

    void sendSms(User user, String message);

    void sendAsyncSms(String phone, String message);

    void sendAsyncSms(User user, String message);
}
