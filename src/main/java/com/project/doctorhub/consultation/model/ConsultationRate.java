package com.project.doctorhub.consultation.model;

import com.project.doctorhub.base.model.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "consultation_rate")
public class ConsultationRate extends BaseEntity<Long> {

    @OneToOne
    @JoinColumn(name = "consultation_id")
    private Consultation consultation;

    @Column(name = "rate")
    private Integer rate;

}
