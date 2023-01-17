package com.project.doctorhub.consultation.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.consultation.model.Consultation;
import com.project.doctorhub.consultation.model.ConsultationRate;
import com.project.doctorhub.consultation.repository.ConsultationRateRepository;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.service.DoctorService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsultationRateService
        extends AbstractCrudService<ConsultationRate, Long, ConsultationRateRepository> {

    private final DoctorService doctorService;
    private final ConsultationRateRepository consultationRateRepository;

    public ConsultationRateService(
            ConsultationRateRepository abstractRepository,
            DoctorService doctorService
    ) {
        super(abstractRepository);
        this.doctorService = doctorService;
        this.consultationRateRepository = abstractRepository;
    }

    public void create(Consultation consultation, Integer rate) {
        ConsultationRate consultationRate = new ConsultationRate();
        consultationRate.setConsultation(consultation);
        consultationRate.setRate(rate);
        consultationRate.setIsDeleted(false);
        save(consultationRate);
        updateDoctorRate(consultation.getDoctor());
    }

    @Async
    public void updateDoctorRate(Doctor doctor) {
        List<Integer> doctorRates = consultationRateRepository.findAllByDoctor(doctor);
        Integer sum = 0;
        for (Integer rate : doctorRates) {
            sum += rate;
        }
        Float finalRate = sum == 0 ? 0f : sum / doctorRates.size();
        doctor.setRate(finalRate);
        doctorService.save(doctor);
    }

}
