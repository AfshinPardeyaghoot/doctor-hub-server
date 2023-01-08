package com.project.doctorhub.consultation.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.chat.model.Chat;
import com.project.doctorhub.chat.service.ChatService;
import com.project.doctorhub.consultation.dto.ConsultationCreateDTO;
import com.project.doctorhub.consultation.model.Consultation;
import com.project.doctorhub.consultation.model.ConsultationStatusType;
import com.project.doctorhub.consultation.repository.ConsultationRepository;
import com.project.doctorhub.doctor.model.DoctorConsultationType;
import com.project.doctorhub.doctor.service.DoctorConsultationTypeService;
import com.project.doctorhub.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultationService
        extends AbstractCrudService<Consultation, Long, ConsultationRepository> {

    private final ChatService chatService;
    private final ConsultationRepository consultationRepository;
    private final DoctorConsultationTypeService doctorConsultationTypeService;

    public ConsultationService(
            ChatService chatService,
            ConsultationRepository abstractRepository,
            DoctorConsultationTypeService doctorConsultationTypeService
    ) {
        super(abstractRepository);
        this.chatService = chatService;
        this.consultationRepository = abstractRepository;
        this.doctorConsultationTypeService = doctorConsultationTypeService;
    }

    @Transactional
    public synchronized Consultation create(User user, ConsultationCreateDTO consultationCreateDTO) {
        DoctorConsultationType doctorConsultationType = doctorConsultationTypeService.findByUUIDNotDeleted(consultationCreateDTO.getDoctorConsultationId());
        Consultation consultation = new Consultation();
        consultation.setStatus(ConsultationStatusType.IN_PROCESS);
        consultation.setUser(user);
        consultation.setIsDeleted(false);
        consultation.setConsultationType(doctorConsultationType.getConsultationType());
        consultation.setDoctor(doctorConsultationType.getDoctor());
        consultation.setPrice(doctorConsultationType.getPrice());
        consultation = save(consultation);
        Chat chat = chatService.create(consultation);
        consultation.setChat(chat);
        return consultation;
    }

    public Page<Consultation> findAllByUserAndStatusNotDeleted(User user, ConsultationStatusType status, Pageable pageable) {
        if (status != null)
            return consultationRepository.findByUserAndStatusAndIsDeletedFalse(user, status, pageable);
        return consultationRepository.findByUserAndIsDeletedFalse(user, pageable);
    }
}
