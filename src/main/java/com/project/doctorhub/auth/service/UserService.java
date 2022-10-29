package com.project.doctorhub.auth.service;

import com.project.doctorhub.auth.model.ApplicationUserDetails;
import com.project.doctorhub.auth.model.User;
import com.project.doctorhub.auth.repository.UserRepository;
import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.service.AbstractCrudService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserService
        extends AbstractCrudService<User, Long, UserRepository> {

    private final UserRepository userRepository;

    public UserService(UserRepository abstractRepository) {
        super(abstractRepository);
        this.userRepository = abstractRepository;
    }

    public User findByPhone(String phone) {
        return userRepository.findByPhone(phone)
                .orElseThrow(() -> new NotFoundException("کاربر یافت نشد!"));
    }
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        User user = userRepository.findByPhone(phone)
                .orElseThrow(() -> new UsernameNotFoundException("user not found!"));

        return new ApplicationUserDetails(user);
    }
}
