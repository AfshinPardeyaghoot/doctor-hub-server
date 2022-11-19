package com.project.doctorhub.auth.service;

import com.project.doctorhub.auth.model.Role;
import com.project.doctorhub.user.model.User;
import com.project.doctorhub.auth.model.UserRole;
import com.project.doctorhub.auth.repository.UserRoleRepository;
import com.project.doctorhub.base.service.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService
        extends AbstractCrudService<UserRole, Long, UserRoleRepository> {

    public UserRoleService(UserRoleRepository abstractRepository) {
        super(abstractRepository);
    }

    public UserRole create(User user, Role role) {
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setIsDeleted(false);
        return save(userRole);
    }
}
