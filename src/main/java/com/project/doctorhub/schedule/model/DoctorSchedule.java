package com.project.doctorhub.schedule.model;

import com.project.doctorhub.base.model.BaseEntity;
import com.project.doctorhub.doctor.model.Doctor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class DoctorSchedule extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;
    @Column(name = "start_hour", nullable = false)
    private String startHour;
    @Column(name = "end_hour", nullable = false)
    private String endHour;
    @Column(name = "day")
    @Enumerated(EnumType.STRING)
    private DayOfWeek day;


}
