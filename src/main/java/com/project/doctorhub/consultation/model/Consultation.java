package com.project.doctorhub.consultation.model;

import com.project.doctorhub.base.model.BaseEntity;
import com.project.doctorhub.chat.model.Chat;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.user.model.User;
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
@Table(name = "consultation")
public class Consultation extends BaseEntity<Long> {

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "price")
    private Long price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "consultation_type_id")
    private ConsultationType consultationType;

    @OneToOne(mappedBy = "consultation", fetch = FetchType.LAZY)
    private Chat chat;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ConsultationStatusType status;

    @Column(name = "start_at")
    private Instant startAt;

    @Column(name = "end_at")
    private Instant endAt;


}
