package com.project.doctorhub.consultation.service;

import com.project.doctorhub.base.exception.NotFoundException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.consultation.model.ConsultationType;
import com.project.doctorhub.consultation.repository.ConsultationTypeRepository;
import org.springframework.stereotype.Service;

@Service
public class ConsultationTypeService
        extends AbstractCrudService<ConsultationType, Long, ConsultationTypeRepository> {

    private final ConsultationTypeRepository consultationTypeRepository;

    public ConsultationTypeService(
            ConsultationTypeRepository abstractRepository
    ) {
        super(abstractRepository);
        this.consultationTypeRepository = abstractRepository;
    }

    public ConsultationType findByNameNotDeleted(String name) {
        return consultationTypeRepository.findByNameIgnoreCaseAndIsDeletedFalse(name)
                .orElseThrow(() -> new NotFoundException("consultation type not found!"));
    }


    public void seeder() {

        if (consultationTypeRepository.findByNameIgnoreCase("text").isEmpty()) {
            ConsultationType textConsultation = new ConsultationType();
            textConsultation.setName("text");
            textConsultation.setTitle("متنی");
            textConsultation.setIsDeleted(false);
            save(textConsultation);
        }

        if (consultationTypeRepository.findByNameIgnoreCase("voice").isEmpty()) {
            ConsultationType voiceConsultation = new ConsultationType();
            voiceConsultation.setName("voice");
            voiceConsultation.setTitle("ویس کال");
            voiceConsultation.setIsDeleted(false);
            save(voiceConsultation);
        }
    }


}
