package com.project.doctorhub.consultation.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.consultation.model.Consultation;
import com.project.doctorhub.consultation.model.ConsultationStatusType;
import com.project.doctorhub.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsultationRepository extends AbstractRepository<Consultation, Long> {

    @Query("select c from Consultation c " +
            "inner join Doctor d on c.doctor = d " +
            "where (d.user = :user or c.user =:user) " +
            "and c.isDeleted = false " +
            "and c.status = :status ")
    Page<Consultation> findByUserAndStatusAndIsDeletedFalse(User user, ConsultationStatusType status, Pageable pageable);

    @Query("select c from Consultation c " +
            "inner join Doctor d on c.doctor = d " +
            "where (d.user = :user or c.user =:user) " +
            "and c.isDeleted = false order by c.createdAt desc ")
    Page<Consultation> findByUserAndIsDeletedFalse(User user, Pageable pageable);


    @Query("select c from Consultation c " +
            "inner join Doctor d on c.doctor = d " +
            "where (d.user = :user or c.user =:user) " +
            "and c.isDeleted = false " +
            "and c.status = :status order by c.createdAt desc")
    List<Consultation> findAllByUserAndStatusAndIsDeletedFalse(User user, ConsultationStatusType status);
}
