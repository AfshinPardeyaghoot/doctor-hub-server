package com.project.doctorhub.consultation.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.consultation.dto.ConsultationCreateDTO;
import com.project.doctorhub.consultation.dto.ConsultationDTOMapper;
import com.project.doctorhub.consultation.dto.ConsultationGetDTO;
import com.project.doctorhub.consultation.model.Consultation;
import com.project.doctorhub.consultation.model.ConsultationStatusType;
import com.project.doctorhub.consultation.service.ConsultationService;
import com.project.doctorhub.user.model.User;
import com.project.doctorhub.user.service.UserService;
import lombok.AllArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/consultation")
public class ConsultationController {

    private final UserService userService;
    private final ConsultationService consultationService;
    private final ConsultationDTOMapper consultationDTOMapper;

    @PostMapping
    public ResponseEntity<HttpResponse<ConsultationGetDTO>> createConsultation(
            Authentication authentication,
            @Valid @RequestBody ConsultationCreateDTO consultationCreateDTO
    ) {
        User user = userService.findByAuthentication(authentication);
        Consultation consultation = consultationService.create(user, consultationCreateDTO);
        ConsultationGetDTO consultationGetDTO = consultationDTOMapper.entityToGetDTO(consultation);
        return ResponseEntity.ok(new HttpResponse<>(consultationGetDTO));
    }

    @GetMapping("/user")
    public ResponseEntity<HttpResponse<Page<ConsultationGetDTO>>> getUserConsultations(
            Authentication authentication,
            @RequestParam(required = false) ConsultationStatusType status,
            @PageableDefault Pageable pageable
    ) {
        User user = userService.findByAuthentication(authentication);
        Page<Consultation> consultations = consultationService.findAllByUserAndStatusNotDeleted(user, status, pageable);
        Page<ConsultationGetDTO> consultationGetDTOS = consultations.map(consultationDTOMapper::entityToGetDTO);
        return ResponseEntity.ok(new HttpResponse<>(consultationGetDTOS));
    }

}
