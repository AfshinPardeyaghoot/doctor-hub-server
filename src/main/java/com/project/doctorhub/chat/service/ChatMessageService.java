package com.project.doctorhub.chat.service;

import com.project.doctorhub.base.config.ApplicationBaseProperties;
import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.chat.dto.ChatMessageFilePostDTO;
import com.project.doctorhub.chat.dto.ChatMessageSendDTO;
import com.project.doctorhub.chat.model.Chat;
import com.project.doctorhub.chat.model.ChatMessage;
import com.project.doctorhub.chat.model.ChatMessageContentType;
import com.project.doctorhub.chat.model.ChatMessageFile;
import com.project.doctorhub.chat.repository.ChatMessageRepository;
import com.project.doctorhub.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChatMessageService
        extends AbstractCrudService<ChatMessage, Long, ChatMessageRepository> {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageFileService chatMessageFileService;

    public ChatMessageService(
            ChatMessageRepository abstractRepository,
            ChatMessageFileService chatMessageFileService
    ) {
        super(abstractRepository);
        chatMessageRepository = abstractRepository;
        this.chatMessageFileService = chatMessageFileService;
    }

    @Transactional
    public ChatMessage create(User user, Chat chat, ChatMessageFilePostDTO chatMessageFilePostDTO) {
        ChatMessage chatMessage = create(user, chat, null, chatMessageFilePostDTO.getContentType());
        ChatMessageFile chatMessageFile = chatMessageFileService.create(chatMessage, chatMessageFilePostDTO);
        chatMessage.setChatMessageFile(chatMessageFile);
        return chatMessage;
    }

    public ChatMessage create(User user, Chat chat, ChatMessageSendDTO chatMessageSendDTO) {
        return create(user, chat, chatMessageSendDTO.getContent(), chatMessageSendDTO.getContentType());
    }

    private ChatMessage create(User user, Chat chat, String content, ChatMessageContentType contentType) {
        ChatMessage chatMessage = new ChatMessage();
        chatMessage.setChat(chat);
        chatMessage.setSendBy(user);
        chatMessage.setContent(content);
        chatMessage.setContentType(contentType);
        chatMessage.setIsDeleted(false);
        return save(chatMessage);
    }

    @Transactional(readOnly = true)
    public Page<ChatMessage> findAllByChatNotDeleted(Chat chat, Pageable pageable) {
        return chatMessageRepository.findByChatAndIsDeletedFalseOrderByCreatedAtDesc(chat, pageable);
    }
}
