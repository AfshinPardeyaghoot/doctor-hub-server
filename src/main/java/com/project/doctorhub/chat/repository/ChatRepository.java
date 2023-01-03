package com.project.doctorhub.chat.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.chat.model.Chat;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRepository
        extends AbstractRepository<Chat, Long> {

}
