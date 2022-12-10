package com.project.doctorhub.consultation.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.consultation.model.ConsultationType;

import java.util.Optional;

public interface ConsultationTypeRepository
        extends AbstractRepository<ConsultationType, Long> {


    Optional<ConsultationType> findByNameIgnoreCase(String name);

    Optional<ConsultationType> findByNameIgnoreCaseAndIsDeletedFalse(String name);

}
