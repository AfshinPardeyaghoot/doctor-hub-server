package com.project.doctorhub.schedule.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.schedule.model.DoctorAvailableDay;
import com.project.doctorhub.schedule.model.DoctorSchedule;
import com.project.doctorhub.schedule.repository.DoctorAvailableDayRepository;
import com.project.doctorhub.util.InstantUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
public class DoctorAvailableDayService
        extends AbstractCrudService<DoctorAvailableDay, Long, DoctorAvailableDayRepository> {


    private final DoctorScheduleService doctorScheduleService;
    private final DoctorAvailableDayRepository doctorAvailableDayRepository;

    public DoctorAvailableDayService(
            DoctorAvailableDayRepository abstractRepository,
            DoctorScheduleService doctorScheduleService
    ) {
        super(abstractRepository);
        this.doctorScheduleService = doctorScheduleService;
        this.doctorAvailableDayRepository = abstractRepository;
    }

    public boolean isDoctorAvailableNow(Doctor doctor) {
        calculateDoctorAvailableTimes(doctor, Instant.now().plus(3, ChronoUnit.DAYS));
        return findByDoctorAndDateBetween(doctor, Instant.now());
    }

    public void deleteAllByDoctor(Doctor doctor){
        doctorAvailableDayRepository.deleteAllByDoctor(doctor);
    }


    public boolean findByDoctorAndDateBetween(Doctor doctor, Instant date) {
        return doctorAvailableDayRepository.findByDoctorAndDateBetween(doctor, date).stream().findAny().isPresent();
    }

    private void calculateDoctorAvailableTimes(Doctor doctor, Instant endTime) {

        Instant lastGeneratedAppointmentTime = InstantUtil.getInstantWithoutHour(getLastGeneratedAvailableTimeByDoctor(doctor));
        List<DoctorSchedule> doctorSchedules = doctorScheduleService.findAllByDoctorNotDeleted(doctor.getUUID());
        doctorSchedules.forEach(doctorSchedule -> {

            List<Instant> availableDates = InstantUtil.getDateFromDayOfWeek(doctorSchedule.getDay(), endTime, lastGeneratedAppointmentTime);

            availableDates.forEach(date -> {

                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                String inputDate = dateFormat.format(date.toEpochMilli());
                Instant start = InstantUtil.getInstantFromSting(inputDate + " " + doctorSchedule.getStartHour());
                Instant end = InstantUtil.getInstantFromSting(inputDate + " " + doctorSchedule.getEndHour());

                List<DoctorAvailableDay> doctorAppointments = new ArrayList<>();
                doctorAppointments.add(create(doctor, start, end));
                saveAll(doctorAppointments);

            });
        });
    }

    private DoctorAvailableDay create(Doctor doctor, Instant start, Instant end) {
        DoctorAvailableDay doctorAvailableDay = new DoctorAvailableDay();
        doctorAvailableDay.setDoctor(doctor);
        doctorAvailableDay.setStartHour(start);
        doctorAvailableDay.setEndHour(end);
        doctorAvailableDay.setIsDeleted(false);
        return doctorAvailableDay;
    }

    @Transactional
    public List<DoctorAvailableDay> saveAll(List<DoctorAvailableDay> doctorAvailableDays) {
        return doctorAvailableDayRepository.saveAll(doctorAvailableDays);
    }


    private Instant getLastGeneratedAvailableTimeByDoctor(Doctor doctor) {
        Instant time = doctorAvailableDayRepository.getDoctorMaxEndDate(doctor);
        return time == null ? Instant.now() : time;
    }
}
