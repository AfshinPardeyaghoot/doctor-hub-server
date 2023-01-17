package com.project.doctorhub.schedule.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.schedule.model.DoctorAvailableDay;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.Optional;

public interface DoctorAvailableDayRepository
        extends AbstractRepository<DoctorAvailableDay, Long> {

    @Query("select max (dad.endHour) from DoctorAvailableDay dad where dad.doctor = :doctor and dad.isDeleted = false ")
    Instant getDoctorMaxEndDate(Doctor doctor);

    @Query("select dad from DoctorAvailableDay dad where dad.doctor = :doctor and dad.startHour < :date and dad.endHour < :date")
    Optional<DoctorAvailableDay> findByDoctorAndDateBetween(Doctor doctor, Instant date);
}
