package com.project.doctorhub.auth.service;

import com.project.doctorhub.auth.dto.AuthenticationTokenDTO;
import com.project.doctorhub.auth.model.Role;
import com.project.doctorhub.auth.model.User;
import com.project.doctorhub.auth.model.UserRole;
import com.project.doctorhub.auth.repository.UserRepository;
import com.project.doctorhub.auth.util.JWTUtil;
import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.model.ApplicationProperties;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.sms.service.SmsService;
import com.project.doctorhub.util.StringUtil;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class UserService
        extends AbstractCrudService<User, Long, UserRepository> {

    private final JWTUtil jwtUtil;
    private final StringUtil stringUtil;
    private final SmsService smsService;
    private final RoleService roleService;
    private final UserRepository userRepository;
    private final UserRoleService userRoleService;
    private final RefreshTokenService refreshTokenService;
    private final ApplicationProperties applicationProperties;

    public UserService(
            JWTUtil jwtUtil,
            StringUtil stringUtil,
            SmsService smsService,
            RoleService roleService,
            UserRoleService userRoleService,
            UserRepository abstractRepository,
            @Lazy RefreshTokenService refreshTokenService,
            ApplicationProperties applicationProperties
    ) {
        super(abstractRepository);
        this.jwtUtil = jwtUtil;
        this.stringUtil = stringUtil;
        this.smsService = smsService;
        this.roleService = roleService;
        this.userRoleService = userRoleService;
        this.userRepository = abstractRepository;
        this.refreshTokenService = refreshTokenService;
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

    public AuthenticationTokenDTO refreshTokens(String refreshToken) {
        User user = refreshTokenService.getRefreshTokenUser(refreshToken);
        return jwtUtil.generateAuthenticationToken(user);
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
        user.setIsDeleted(false);
        save(user);
        addUserRole(user);
        return user;
    }

    private void addUserRole(User user) {
        Role role = roleService.getByName(Role.USER_ROLE);
        UserRole userRole = userRoleService.create(user, role);
        userRole.getUser().getUserRoles().add(userRole);
        user.getUserRoles().add(userRole);
    }
}
