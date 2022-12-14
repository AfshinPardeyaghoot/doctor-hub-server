package com.project.doctorhub.schedule.repository;


import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.schedule.model.DayOfWeek;
import com.project.doctorhub.schedule.model.DoctorSchedule;

import java.util.List;
import java.util.Optional;

public interface DoctorScheduleRepository extends AbstractRepository<DoctorSchedule, Long> {

    Optional<DoctorSchedule> findByDoctorAndDay(Doctor doctor, DayOfWeek day);

    Optional<DoctorSchedule> findByDoctorAndDayAndIsDeletedFalse(Doctor doctor, DayOfWeek day);

    List<DoctorSchedule> findAllByDoctorAndIsDeletedFalse(Doctor doctor);


}
