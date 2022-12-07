package com.project.doctorhub.doctor.service;

import com.project.doctorhub.auth.model.Role;
import com.project.doctorhub.base.exception.HttpException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.doctor.dto.DoctorCreateDTO;
import com.project.doctorhub.doctor.dto.DoctorUpdateDTO;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.repository.DoctorRepository;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.service.SpecialityService;
import com.project.doctorhub.storageFile.model.StorageFile;
import com.project.doctorhub.storageFile.model.StorageFileType;
import com.project.doctorhub.storageFile.service.StorageFileService;
import com.project.doctorhub.user.model.User;
import com.project.doctorhub.user.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class DoctorService
        extends AbstractCrudService<Doctor, Long, DoctorRepository> {

    private final UserService userService;
    private final DoctorRepository doctorRepository;
    private final SpecialityService specialityService;
    private final StorageFileService storageFileService;

    public DoctorService(
            DoctorRepository abstractRepository,
            UserService userService,
            StorageFileService storageFileService,
            SpecialityService specialityService,
            DoctorRepository doctorRepository
    ) {
        super(abstractRepository);
        this.doctorRepository = doctorRepository;
        this.userService = userService;
        this.specialityService = specialityService;
        this.storageFileService = storageFileService;
    }


    @Transactional
    public Doctor create(DoctorCreateDTO doctorCreateDTO) {

        if (doctorRepository.findByPhone(doctorCreateDTO.getPhone()).isPresent()) {
            throw new HttpException("در حال حاضر پزشکی با شماره وارد شده وجود دارد!", HttpStatus.BAD_REQUEST);
        }

        User user = userService.createOrFetch(
                doctorCreateDTO.getPhone(),
                Role.DOCTOR
        );

        StorageFile profileImage = storageFileService.create(
                doctorCreateDTO.getProfileImage(),
                StorageFileType.PROFILE_IMAGE
        );

        Speciality speciality = specialityService.findByUUIDNotDeleted(doctorCreateDTO.getSpecialityId());

        Doctor doctor = create(
                user,
                doctorCreateDTO.getDescription(),
                doctorCreateDTO.getGmcNumber(),
                profileImage,
                speciality
        );


        return doctor;
    }

    private Doctor create(User user, String description, String gmcNumber, StorageFile profileImage, Speciality speciality) {
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setDescription(description);
        doctor.setGmcNumber(gmcNumber);
        doctor.setProfileImage(profileImage);
        doctor.setIsDeleted(false);
        doctor.setSpeciality(speciality);
        return save(doctor);
    }

    public Doctor update(String uuid, DoctorUpdateDTO doctorUpdateDTO) {
        Doctor doctor = findByUUIDNotDeleted(uuid);
        if (doctorUpdateDTO.getDescription() != null)
            doctor.setDescription(doctorUpdateDTO.getDescription());

        if (doctorUpdateDTO.getGmcNumber() != null)
            doctor.setGmcNumber(doctorUpdateDTO.getGmcNumber());

        if (doctorUpdateDTO.getProfileImage() != null
                && !doctorUpdateDTO.getProfileImage().isEmpty()) {

            StorageFile profileImage = storageFileService.create(
                    doctorUpdateDTO.getProfileImage(),
                    StorageFileType.PROFILE_IMAGE
            );
            doctor.setProfileImage(profileImage);
        }

        if (doctorUpdateDTO.getSpecialityId() != null) {
            Speciality speciality = specialityService.findByUUIDNotDeleted(doctorUpdateDTO.getSpecialityId());
            doctor.setSpeciality(speciality);
        }

        return save(doctor);
    }

    public Page<Doctor> findAllByNameLike(String name, Pageable pageable) {
        if (name != null)
            return doctorRepository.findAllByNameLike(name, pageable);
        return doctorRepository.findAllByIsDeleted(false, pageable);
    }


    public void seeder() {

    }
}
