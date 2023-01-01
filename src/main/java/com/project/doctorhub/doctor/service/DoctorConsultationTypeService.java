package com.project.doctorhub.doctor.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.consultation.model.ConsultationType;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.model.DoctorConsultationType;
import com.project.doctorhub.doctor.repository.DoctorConsultationTypeRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorConsultationTypeService
        extends AbstractCrudService<DoctorConsultationType, Long, DoctorConsultationTypeRepository> {

    private final DoctorConsultationTypeRepository doctorConsultationTypeRepository;

    public DoctorConsultationTypeService(DoctorConsultationTypeRepository abstractRepository) {
        super(abstractRepository);
        this.doctorConsultationTypeRepository = abstractRepository;
    }


    public DoctorConsultationType create(Doctor doctor, ConsultationType consultationType, Long price) {
        Optional<DoctorConsultationType> doctorConsultationTypeOptional = doctorConsultationTypeRepository.findByDoctorAndConsultationType(doctor, consultationType);
        DoctorConsultationType doctorConsultationType;
        if (doctorConsultationTypeOptional.isPresent()) {
            doctorConsultationType = doctorConsultationTypeOptional.get();
            doctorConsultationType.setPrice(price);
            doctorConsultationType.setIsDeleted(false);
        } else {
            doctorConsultationType = new DoctorConsultationType();
            doctorConsultationType.setConsultationType(consultationType);
            doctorConsultationType.setDoctor(doctor);
            doctorConsultationType.setIsDeleted(false);
            doctorConsultationType.setPrice(price);
        }

        return save(doctorConsultationType);
    }
}
