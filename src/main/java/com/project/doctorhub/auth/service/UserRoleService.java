package com.project.doctorhub.auth.service;

import com.project.doctorhub.auth.model.UserRole;
import com.project.doctorhub.auth.repository.UserRoleRepository;
import com.project.doctorhub.base.service.AbstractCrudService;
import org.springframework.stereotype.Service;

@Service
public class UserRoleService extends AbstractCrudService<UserRole, Long, UserRoleRepository> {

    public UserRoleService(UserRoleRepository abstractRepository) {
        super(abstractRepository);
    }
}
