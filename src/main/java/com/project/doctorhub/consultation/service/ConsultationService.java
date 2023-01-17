package com.project.doctorhub.consultation.service;

import com.project.doctorhub.base.exception.HttpException;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.chat.model.Chat;
import com.project.doctorhub.chat.service.ChatService;
import com.project.doctorhub.consultation.dto.ConsultationCreateDTO;
import com.project.doctorhub.consultation.model.Consultation;
import com.project.doctorhub.consultation.model.ConsultationStatusType;
import com.project.doctorhub.consultation.repository.ConsultationRepository;
import com.project.doctorhub.doctor.model.Doctor;
import com.project.doctorhub.doctor.model.DoctorConsultationType;
import com.project.doctorhub.doctor.service.DoctorConsultationTypeService;
import com.project.doctorhub.schedule.service.DoctorAvailableDayService;
import com.project.doctorhub.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConsultationService
        extends AbstractCrudService<Consultation, Long, ConsultationRepository> {

    private final ChatService chatService;
    private final ConsultationRepository consultationRepository;
    private final DoctorAvailableDayService doctorAvailableDayService;
    private final DoctorConsultationTypeService doctorConsultationTypeService;

    public ConsultationService(
            ChatService chatService,
            ConsultationRepository abstractRepository,
            DoctorConsultationTypeService doctorConsultationTypeService,
            DoctorAvailableDayService doctorAvailableDayService
    ) {
        super(abstractRepository);
        this.chatService = chatService;
        this.consultationRepository = abstractRepository;
        this.doctorAvailableDayService = doctorAvailableDayService;
        this.doctorConsultationTypeService = doctorConsultationTypeService;
    }

    @Transactional
    public synchronized Consultation create(User user, ConsultationCreateDTO consultationCreateDTO) {
        checkUserHaveNotConsultation(user);
        DoctorConsultationType doctorConsultationType = doctorConsultationTypeService.findByUUIDNotDeleted(consultationCreateDTO.getDoctorConsultationId());
        checkDoctorConsultationCount(doctorConsultationType.getDoctor());
        checkDoctorIsOnline(doctorConsultationType);
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

    private void checkDoctorConsultationCount(Doctor doctor) {
        int openConsultationCount = consultationRepository.findAllByUserAndStatusAndIsDeletedFalse(doctor.getUser(), ConsultationStatusType.IN_PROCESS).size();
        if (openConsultationCount >= 2)
            throw new HttpException("پزشک در حال حاظر در حال مشاوره می باشد. لطفا دقایقی دیگر دوباره تلاش نمایید!", HttpStatus.BAD_REQUEST);
    }

    private void checkUserHaveNotConsultation(User user) {
        boolean haveOpenConsultation = consultationRepository.
                findAllByUserAndStatusAndIsDeletedFalse(user, ConsultationStatusType.IN_PROCESS)
                .stream()
                .findAny()
                .isPresent();

        if (haveOpenConsultation)
            throw new HttpException("شما در حال حاظر یک مشاوره در جریان دارید. لطفا پس از اتمام آن دوباره تلاش نمایید!", HttpStatus.BAD_REQUEST);
    }

    private void checkDoctorIsOnline(DoctorConsultationType doctorConsultationType) {
        if (!doctorAvailableDayService.isDoctorAvailableNow(doctorConsultationType.getDoctor())) {
            throw new HttpException("پزشک مورد نظر در دسترسی نیست!", HttpStatus.BAD_REQUEST);
        }
    }

    public Page<Consultation> findAllByUserAndStatusNotDeleted(User user, ConsultationStatusType status, Pageable pageable) {
        if (status != null)
            return consultationRepository.findByUserAndStatusAndIsDeletedFalse(user, status, pageable);
        return consultationRepository.findByUserAndIsDeletedFalse(user, pageable);
    }

    public void setConsultationIsEnd(Consultation consultation) {
        consultation.setStatus(ConsultationStatusType.FINISHED);
        save(consultation);
    }
}
