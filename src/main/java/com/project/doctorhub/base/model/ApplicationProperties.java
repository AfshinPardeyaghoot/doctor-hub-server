package com.project.doctorhub.base.model;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ApplicationProperties {

    private final Integer verificationCodeLength;
    private final Integer verificationCodeExpireAfterMinute;


    public ApplicationProperties(
            @Value("${doctorhub.auth.verificationCode.length}") Integer verificationCodeLength,
            @Value("${doctorhub.auth.verificationCode.expireAfterMinute}") Integer verificationCodeExpireAfterMinute
    ) {
        this.verificationCodeLength = verificationCodeLength;
        this.verificationCodeExpireAfterMinute = verificationCodeExpireAfterMinute;
    }

    public Integer getVerificationCodeLength() {
        return verificationCodeLength;
    }

    public Integer getVerificationCodeExpireAfterMinute() {
        return verificationCodeExpireAfterMinute;
    }
}
