package com.project.doctorhub.schedule.model;

import com.project.doctorhub.base.model.BaseEntity;
import com.project.doctorhub.doctor.model.Doctor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "doctor_available_day")
public class DoctorAvailableDay
        extends BaseEntity<Long> {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "start_hour")
    private Instant startHour;

    @Column(name = "end_hour")
    private Instant endHour;


}
