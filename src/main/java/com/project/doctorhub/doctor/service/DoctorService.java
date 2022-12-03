package com.project.doctorhub.doctor.service;

import com.project.doctorhub.auth.model.Role;
import com.project.doctorhub.base.exception.HttpException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.dto.DoctorCreateDTO;
import com.project.doctorhub.doctor.repository.DoctorRepository;
import com.project.doctorhub.image.model.StorageFile;
import com.project.doctorhub.image.model.StorageFileType;
import com.project.doctorhub.image.service.StorageFileService;
import com.project.doctorhub.user.model.User;
import com.project.doctorhub.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class DoctorService
        extends AbstractCrudService<Doctor, Long, DoctorRepository> {

    private final UserService userService;
    private final DoctorRepository doctorRepository;
    private final StorageFileService storageFileService;
    private final DoctorSpecialityService doctorSpecialityService;

    public DoctorService(
            DoctorRepository abstractRepository,
            UserService userService,
            StorageFileService storageFileService,
            DoctorRepository doctorRepository,
            DoctorSpecialityService doctorSpecialityService
    ) {
        super(abstractRepository);
        this.doctorRepository = doctorRepository;
        this.userService = userService;
        this.storageFileService = storageFileService;
        this.doctorSpecialityService = doctorSpecialityService;
    }


    @Transactional
    public Doctor create(DoctorCreateDTO doctorCreateDTO) {

        if (doctorRepository.findByPhone(doctorCreateDTO.getPhone()).isPresent()){
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

        Doctor doctor = create(
                user,
                doctorCreateDTO.getDescription(),
                doctorCreateDTO.getGmcNumber(),
                profileImage
        );

        doctorSpecialityService.addDoctorSpeciality(doctor, doctorCreateDTO.getSpeciality());

        return doctor;
    }

    private Doctor create(User user, String description, String gmcNumber, StorageFile profileImage){
        Doctor doctor = new Doctor();
        doctor.setUser(user);
        doctor.setDescription(description);
        doctor.setGmcNumber(gmcNumber);
        doctor.setProfileImage(profileImage);
        doctor.setIsDeleted(false);
        return save(doctor);
    }
}
