package com.project.doctorhub.schedule.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.service.DoctorService;
import com.project.doctorhub.schedule.dto.DoctorScheduleAddDTO;
import com.project.doctorhub.schedule.model.DoctorSchedule;
import com.project.doctorhub.schedule.repository.DoctorScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class DoctorScheduleService
        extends AbstractCrudService<DoctorSchedule, Long, DoctorScheduleRepository> {

    private final DoctorService doctorService;
    private final DoctorScheduleRepository doctorScheduleRepository;

    public DoctorScheduleService(
            DoctorScheduleRepository abstractRepository,
            DoctorService doctorService
    ) {
        super(abstractRepository);
        this.doctorService = doctorService;
        this.doctorScheduleRepository = abstractRepository;
    }


    public DoctorSchedule create(DoctorScheduleAddDTO doctorScheduleAddDTO) {
        Doctor doctor = doctorService.findByPhoneNotDeleted(doctorScheduleAddDTO.getDoctorPhone());
        Optional<DoctorSchedule> doctorScheduleOptional = doctorScheduleRepository.findByDoctorAndDay(doctor, doctorScheduleAddDTO.getDay());
        DoctorSchedule doctorSchedule;
        if (doctorScheduleOptional.isPresent()){
            doctorSchedule = doctorScheduleOptional.get();
        }else {
            doctorSchedule = new DoctorSchedule();
            doctorSchedule.setDoctor(doctor);
            doctorSchedule.setDay(doctorScheduleAddDTO.getDay());
        }
        doctorSchedule.setIsDeleted(false);
        doctorSchedule.setEndHour(doctorScheduleAddDTO.getEndHour());
        doctorSchedule.setStartHour(doctorScheduleAddDTO.getStartHour());
        return save(doctorSchedule);
    }

    public DoctorSchedule save(DoctorSchedule doctorSchedule){
        return doctorScheduleRepository.save(doctorSchedule);
    }


}
