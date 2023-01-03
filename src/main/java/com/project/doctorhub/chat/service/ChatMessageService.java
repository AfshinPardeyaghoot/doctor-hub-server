package com.project.doctorhub.chat.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.chat.dto.ChatMessageSendDTO;
import com.project.doctorhub.chat.model.Chat;
import com.project.doctorhub.chat.model.ChatMessage;
import com.project.doctorhub.chat.repository.ChatMessageRepository;
import com.project.doctorhub.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageService
        extends AbstractCrudService<ChatMessage, Long, ChatMessageRepository> {

    private final ChatMessageRepository chatMessageRepository;

    public ChatMessageService(ChatMessageRepository abstractRepository) {
        super(abstractRepository);
        chatMessageRepository = abstractRepository;
    }

    public ChatMessage create(User user, Chat chat, ChatMessageSendDTO chatMessageSendDTO) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChat(chat);
        chatMessage.setSendBy(user);
        chatMessage.setContent(chatMessageSendDTO.getContent());
        chatMessage.setContentType(chatMessageSendDTO.getContentType());
        chatMessage.setIsDeleted(false);
        return save(chatMessage);
    }

    public Page<ChatMessage> findAllByChatNotDeleted(Chat chat, Pageable pageable) {
        return chatMessageRepository.findByChatAndIsDeletedFalseOrderByCreatedAtDesc(chat, pageable);
    }
}
