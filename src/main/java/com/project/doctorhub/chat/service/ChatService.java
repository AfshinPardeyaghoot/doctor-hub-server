package com.project.doctorhub.chat.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.chat.model.Chat;
import com.project.doctorhub.chat.repository.ChatRepository;
import com.project.doctorhub.consultation.model.Consultation;
import org.springframework.stereotype.Service;

@Service
public class ChatService
        extends AbstractCrudService<Chat, Long, ChatRepository> {

    public ChatService(ChatRepository abstractRepository) {
        super(abstractRepository);
    }

    public Chat create(Consultation consultation) {
        Chat chat = new Chat();
        chat.setConsultation(consultation);
        chat.setIsDeleted(false);
        return save(chat);
    }
}
