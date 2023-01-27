package com.project.doctorhub.auth.service;

import com.project.doctorhub.auth.model.Role;
import com.project.doctorhub.auth.repository.RoleRepository;
import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.doctor.model.Doctor;
import org.springframework.stereotype.Service;


@Service
public class RoleService
        extends AbstractCrudService<Role, Long, RoleRepository> {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository abstractRepository) {
        super(abstractRepository);
        this.roleRepository = abstractRepository;
    }

    public Role getByName(String name) {
        return roleRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException(String.format("Role by name %s not found !", name)));
    }

    public void seeder() {
        if (roleRepository.findByNameIgnoreCase(Role.USER_ROLE).isEmpty()){
            Role user = new Role();
            user.setName(Role.USER_ROLE);
            user.setIsDeleted(false);
            save(user);
        }

        if (roleRepository.findByNameIgnoreCase(Role.ADMIN).isEmpty()){
            Role admin = new Role();
            admin.setIsDeleted(false);
            admin.setName(Role.ADMIN);
            save(admin);
        }

        if (roleRepository.findByNameIgnoreCase(Role.DOCTOR).isEmpty()){
            Role doctor = new Role();
            doctor.setName(Role.DOCTOR);
            doctor.setIsDeleted(false);
            save(doctor);
        }
    }
}
