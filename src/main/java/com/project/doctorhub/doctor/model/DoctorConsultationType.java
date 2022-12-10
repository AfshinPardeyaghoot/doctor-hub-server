package com.project.doctorhub.doctor.model;

import com.project.doctorhub.base.model.BaseEntity;
import com.project.doctorhub.consultation.model.ConsultationType;
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
@Table(name = "doctor_consultation_type")
public class DoctorConsultationType
        extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne
    @JoinColumn(name = "consultation_type_id", nullable = false)
    private ConsultationType consultationType;

    @Column(name = "price", nullable = false)
    private Long price;


}
