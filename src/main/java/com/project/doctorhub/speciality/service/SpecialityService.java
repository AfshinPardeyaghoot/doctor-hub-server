package com.project.doctorhub.speciality.service;

import com.project.doctorhub.base.exception.HttpException;
import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.speciality.dto.SpecialityUpdateDTO;
import com.project.doctorhub.storageFile.model.StorageFile;
import com.project.doctorhub.storageFile.model.StorageFileType;
import com.project.doctorhub.storageFile.service.StorageFileService;
import com.project.doctorhub.speciality.dto.SpecialityCreateDTO;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.repository.SpecialityRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class SpecialityService
        extends AbstractCrudService<Speciality, Long, SpecialityRepository> {

    private final StorageFileService storageFileService;
    private final SpecialityRepository specialityRepository;

    public SpecialityService(
            SpecialityRepository abstractRepository,
            StorageFileService storageFileService
    ) {
        super(abstractRepository);
        this.specialityRepository = abstractRepository;
        this.storageFileService = storageFileService;
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

        StorageFile specialityImage = storageFileService.create(
                specialityCreateDTO.getImage(),
                StorageFileType.SPECIALITY_IMAGE
        );
        return create(specialityCreateDTO, specialityImage);
    }

    private Speciality create(SpecialityCreateDTO specialityCreateDTO, StorageFile specialityImage) {
        Speciality speciality = new Speciality();
        speciality.setImage(specialityImage);
        speciality.setName(specialityCreateDTO.getName().toLowerCase());
        speciality.setTitle(specialityCreateDTO.getTitle());
        speciality.setIsDeleted(false);
        return save(speciality);
    }

    public Speciality update(String uuid, SpecialityUpdateDTO specialityUpdateDTO) {
        Speciality speciality = findByUUIDNotDeleted(uuid);

        if (specialityUpdateDTO.getTitle() != null)
            speciality.setTitle(specialityUpdateDTO.getTitle());

        if (specialityUpdateDTO.getImage() != null) {
            StorageFile specialityImage = storageFileService.create(
                    specialityUpdateDTO.getImage(),
                    StorageFileType.SPECIALITY_IMAGE
            );
            speciality.setImage(specialityImage);
        }

        return save(speciality);
    }
}
