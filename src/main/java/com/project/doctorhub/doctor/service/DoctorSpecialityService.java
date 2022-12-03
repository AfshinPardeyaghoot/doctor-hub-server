package com.project.doctorhub.doctor.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.model.DoctorSpeciality;
import com.project.doctorhub.doctor.repository.DoctorSpecialityRepository;
import com.project.doctorhub.speciality.model.Speciality;
import com.project.doctorhub.speciality.service.SpecialityService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DoctorSpecialityService
        extends AbstractCrudService<DoctorSpeciality, Long, DoctorSpecialityRepository> {

    private final SpecialityService specialityService;
    private final DoctorSpecialityRepository doctorSpecialityRepository;

    public DoctorSpecialityService(
            DoctorSpecialityRepository abstractRepository,
            DoctorSpecialityRepository doctorSpecialityRepository,
            SpecialityService specialityService
    ) {
        super(abstractRepository);
        this.specialityService = specialityService;
        this.doctorSpecialityRepository = doctorSpecialityRepository;
    }


    @Transactional
    public void addDoctorSpeciality(Doctor doctor, String specialityName) {
        Speciality speciality = specialityService.findByName(specialityName);
        DoctorSpeciality doctorSpeciality = doctorSpecialityRepository.findByDoctorAndSpeciality(doctor, speciality)
                .orElseGet(()-> create(doctor, speciality));

        if (doctorSpeciality.getIsDeleted()){
            doctorSpeciality.setIsDeleted(false);
            save(doctorSpeciality);
        }

        doctor.getDoctorSpecialities().add(doctorSpeciality);

    }

    private DoctorSpeciality create(Doctor doctor, Speciality speciality) {
        DoctorSpeciality doctorSpeciality = new DoctorSpeciality();
        doctorSpeciality.setDoctor(doctor);
        doctorSpeciality.setSpeciality(speciality);
        doctorSpeciality.setIsDeleted(false);
        return save(doctorSpeciality);
    }

}
