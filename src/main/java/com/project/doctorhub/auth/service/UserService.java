package com.project.doctorhub.auth.service;

import com.project.doctorhub.auth.model.User;
import com.project.doctorhub.auth.repository.UserRepository;
import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.model.ApplicationProperties;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.sms.service.SmsService;
import com.project.doctorhub.util.StringUtil;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class UserService
        extends AbstractCrudService<User, Long, UserRepository> {

    private final StringUtil stringUtil;
    private final SmsService smsService;
    private final UserRepository userRepository;
    private final ApplicationProperties applicationProperties;

    public UserService(
            StringUtil stringUtil,
            SmsService smsService,
            UserRepository abstractRepository,
            ApplicationProperties applicationProperties
    ) {
        super(abstractRepository);
        this.stringUtil = stringUtil;
        this.smsService = smsService;
        this.userRepository = abstractRepository;
        this.applicationProperties = applicationProperties;
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException("کاربر یافت نشد!"));
    }

    public void sendUserAuthenticationCode(String phone) {
        String code = stringUtil.generateRandomNumber(applicationProperties.getVerificationCodeLength());
        User user = createOrFetch(phone);
        setUserAuthenticationCode(user, code);
        sendAuthenticationSms(user, code);
    }

    private void sendAuthenticationSms(User user, String code) {
        String message = String.format("دکترهاب\nکد احراز هویت شما:\n%s", code);
        smsService.sendAsyncSms(user, message);
    }

    private void setUserAuthenticationCode(User user, String code) {
        user.setAuthenticationCode(code);
        user.setAuthenticationCodeExpAt(Instant.now().plus(applicationProperties.getVerificationCodeExpireAfterMinute(), ChronoUnit.MINUTES));
        save(user);
    }


    private User createOrFetch(String phone) {
        return userRepository.findByPhone(phone)
                .orElseGet(() -> create(phone));
    }

    private User create(String phone) {
        User user = new User();
        user.setPhone(phone);
        return save(user);
    }
}
