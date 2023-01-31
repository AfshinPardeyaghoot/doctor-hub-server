package com.project.doctorhub.schedule.service;

import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.service.DoctorService;
import com.project.doctorhub.schedule.dto.DoctorScheduleAddDTO;
import com.project.doctorhub.schedule.dto.DoctorScheduleDeleteDTO;
import com.project.doctorhub.schedule.dto.DoctorScheduleUpdateDTO;
import com.project.doctorhub.schedule.model.DayOfWeek;
import com.project.doctorhub.schedule.model.DoctorSchedule;
import com.project.doctorhub.schedule.repository.DoctorScheduleRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorScheduleService
        extends AbstractCrudService<DoctorSchedule, Long, DoctorScheduleRepository> {

    private final DoctorService doctorService;
    private final DoctorAvailableDayService doctorAvailableDayService;
    private final DoctorScheduleRepository doctorScheduleRepository;

    public DoctorScheduleService(
            DoctorScheduleRepository abstractRepository,
            @Lazy DoctorAvailableDayService doctorAvailableDayService,
            DoctorService doctorService
    ) {
        super(abstractRepository);
        this.doctorService = doctorService;
        this.doctorScheduleRepository = abstractRepository;
        this.doctorAvailableDayService = doctorAvailableDayService;
    }


    public DoctorSchedule createOrUpdate(Doctor doctor, DayOfWeek day, String startHour, String endHour) {
        Optional<DoctorSchedule> doctorScheduleOptional = doctorScheduleRepository.findByDoctorAndDay(doctor, day);
        DoctorSchedule doctorSchedule;
        if (doctorScheduleOptional.isPresent()) {
            doctorSchedule = doctorScheduleOptional.get();
        } else {
            doctorSchedule = new DoctorSchedule();
            doctorSchedule.setDoctor(doctor);
            doctorSchedule.setDay(day);
            doctorSchedule.setOrderIndex(day.getOrder());
        }
        doctorSchedule.setIsDeleted(false);
        doctorSchedule.setEndHour(endHour);
        doctorSchedule.setStartHour(startHour);
        return save(doctorSchedule);
    }

    public DoctorSchedule save(DoctorSchedule doctorSchedule) {
        return doctorScheduleRepository.save(doctorSchedule);
    }


    public void delete(DoctorScheduleDeleteDTO doctorScheduleDeleteDTO) {
        Doctor doctor = doctorService.findByPhoneNotDeleted(doctorScheduleDeleteDTO.getDoctorPhone());
        DoctorSchedule doctorSchedule = findByDoctorAndDayNotDeleted(doctor, doctorScheduleDeleteDTO.getDay());
        safeDelete(doctorSchedule);
    }

    private DoctorSchedule findByDoctorAndDayNotDeleted(Doctor doctor, DayOfWeek day) {
        return doctorScheduleRepository.findByDoctorAndDayAndIsDeletedFalse(doctor, day)
                .orElseThrow(() -> new NotFoundException("doctor schedule not found!"));
    }

    public List<DoctorSchedule> findAllByDoctorNotDeleted(String uuid) {
        Doctor doctor = doctorService.findByUUIDNotDeleted(uuid);
        return doctorScheduleRepository.findAllByDoctorAndIsDeletedFalseOrderByOrderIndexAsc(doctor);
    }

    public DoctorSchedule update(DoctorScheduleAddDTO doctorScheduleAddDTO) {
        Doctor doctor = doctorService.findByPhoneNotDeleted(doctorScheduleAddDTO.getDoctorPhone());
        DoctorSchedule doctorSchedule = findByDoctorAndDayNotDeleted(doctor, doctorScheduleAddDTO.getDay());
        doctorSchedule.setStartHour(doctorScheduleAddDTO.getStartHour());
        doctorSchedule.setEndHour(doctorScheduleAddDTO.getEndHour());
        return save(doctorSchedule);
    }

    public DoctorSchedule create(Doctor doctor, DayOfWeek day, String startHour, String endHour) {
        DoctorSchedule doctorSchedule = new DoctorSchedule();
        doctorSchedule.setDay(day);
        doctorSchedule.setDoctor(doctor);
        doctorSchedule.setStartHour(startHour);
        doctorSchedule.setEndHour(endHour);
        doctorSchedule.setOrderIndex(day.getOrder());
        doctorSchedule.setIsDeleted(false);
        return save(doctorSchedule);
    }

    @Transactional
    public void update(Doctor doctor, List<DoctorScheduleUpdateDTO> schedules) {
        deleteAllDoctorScheduleDays(doctor);
        for (DoctorScheduleUpdateDTO schedule : schedules){
            createOrUpdate(doctor, schedule.getDay(), schedule.getStartHour(), schedule.getEndHour());
        }
    }


    @Transactional
    public void deleteAllDoctorScheduleDays(Doctor doctor) {
        List<DoctorSchedule> doctorSchedules = findAllByDoctorNotDeleted(doctor.getUUID());
        doctorSchedules.forEach(doctorSchedule -> doctorSchedule.setIsDeleted(true));
        doctorScheduleRepository.saveAll(doctorSchedules);
        doctorAvailableDayService.deleteAllByDoctor(doctor);
    }

}
