package com.project.doctorhub.chat.repository;

import com.project.doctorhub.base.repository.AbstractRepository;
import com.project.doctorhub.chat.model.Chat;
import com.project.doctorhub.chat.model.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ChatMessageRepository extends AbstractRepository<ChatMessage, Long> {

    Page<ChatMessage> findByChatAndIsDeletedFalseOrderByCreatedAtDesc(Chat chat, Pageable pageable);

}
