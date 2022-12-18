package com.project.doctorhub.consultation.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.consultation.model.Consultation;
import com.project.doctorhub.consultation.model.ConsultationStatusType;
import com.project.doctorhub.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ConsultationRepository extends AbstractRepository<Consultation, Long> {

    Page<Consultation> findByUserAndStatusAndIsDeletedFalse(User user, ConsultationStatusType status, Pageable pageable);

    Page<Consultation> findByUserAndIsDeletedFalse(User user, Pageable pageable);
}
