package com.project.doctorhub.auth.service;

import com.project.doctorhub.auth.model.Role;
import com.project.doctorhub.auth.repository.RoleRepository;
import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.service.AbstractCrudService;
import org.springframework.stereotype.Service;


@Service
public class RoleService extends AbstractCrudService<Role, Long, RoleRepository> {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository abstractRepository) {
        super(abstractRepository);
        this.roleRepository = abstractRepository;
    }

    public Role getByName(String name) {
        return roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException(String.format("Role by name %s not found !", name)));
    }
}
