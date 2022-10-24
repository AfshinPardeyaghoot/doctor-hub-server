package com.project.doctorhub.auth.service;

import com.project.doctorhub.auth.model.Role;
import com.project.doctorhub.auth.repository.RoleRepository;
import com.project.doctorhub.base.service.AbstractCrudService;
import org.springframework.stereotype.Service;


@Service
public class RoleService extends AbstractCrudService<Role, Long, RoleRepository> {

    public RoleService(RoleRepository abstractRepository) {
        super(abstractRepository);
    }
}
