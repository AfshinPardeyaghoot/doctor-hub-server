package com.project.doctorhub.auth.service;

import com.project.doctorhub.auth.model.Role;
import com.project.doctorhub.auth.model.UserRole;
import com.project.doctorhub.auth.repository.UserRoleRepository;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserRoleService
        extends AbstractCrudService<UserRole, Long, UserRoleRepository> {

    private final UserRoleRepository userRoleRepository;

    public UserRoleService(UserRoleRepository abstractRepository) {
        super(abstractRepository);
        this.userRoleRepository = abstractRepository;
    }

    public UserRole create(User user, Role role) {
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(role);
        userRole.setIsDeleted(false);
        return save(userRole);
    }

    @Transactional
    public void updateUserRole(User user, Role role) {
        deleteUserAllRoles(user);
        create(user, role);
    }

    private void deleteUserAllRoles(User user) {
        List<UserRole> userRoleList = userRoleRepository.findAllByUser(user);
        userRoleRepository.deleteAll(userRoleList);
    }
}
