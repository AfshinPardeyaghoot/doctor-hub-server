package com.project.doctorhub.speciality.service;

import com.project.doctorhub.base.exception.HttpException;
import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.speciality.dto.SpecialityCreateDTO;
import com.project.doctorhub.speciality.dto.SpecialityUpdateDTO;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.repository.SpecialityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class SpecialityService
        extends AbstractCrudService<Speciality, Long, SpecialityRepository> {
    private final SpecialityRepository specialityRepository;

    public SpecialityService(
            SpecialityRepository abstractRepository
    ) {
        super(abstractRepository);
        this.specialityRepository = abstractRepository;
    }

    public Speciality findByName(String name) {
        return specialityRepository.findByNameIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("speciality not found"));
    }

    @Transactional
    public Speciality create(SpecialityCreateDTO specialityCreateDTO) {
        if (specialityRepository.findByNameIgnoreCase(specialityCreateDTO.getName()).isPresent()) {
            throw new HttpException("تخصص وارد شده در حال حاظر موجود است!", HttpStatus.BAD_REQUEST);
        }

        Speciality speciality = new Speciality();
        speciality.setName(specialityCreateDTO.getName().toLowerCase());
        speciality.setTitle(specialityCreateDTO.getTitle());
        speciality.setIsDeleted(false);
        return save(speciality);
    }

    public List<Speciality> findAllByUUIDs(List<String> uuids){
        return specialityRepository.findAllByUUIDInAndIsDeletedFalse(uuids);
    }


    public Speciality update(String uuid, SpecialityUpdateDTO specialityUpdateDTO) {
        Speciality speciality = findByUUIDNotDeleted(uuid);

        if (specialityUpdateDTO.getTitle() != null)
            speciality.setTitle(specialityUpdateDTO.getTitle());

        return save(speciality);
    }


}
