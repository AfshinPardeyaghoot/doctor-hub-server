package com.project.doctorhub.chat.service;

import com.project.doctorhub.base.service.AbstractCrudService;
import com.project.doctorhub.chat.dto.ChatMessageFilePostDTO;
import com.project.doctorhub.chat.model.ChatMessage;
import com.project.doctorhub.chat.model.ChatMessageFile;
import com.project.doctorhub.chat.repository.ChatMessageFileRepository;
import com.project.doctorhub.storageFile.model.StorageFile;
import com.project.doctorhub.storageFile.model.StorageFileType;
import com.project.doctorhub.storageFile.service.StorageFileService;
import org.springframework.stereotype.Service;

@Service
public class ChatMessageFileService
        extends AbstractCrudService<ChatMessageFile, Long, ChatMessageFileRepository> {

    private final StorageFileService storageFileService;

    public ChatMessageFileService(
            ChatMessageFileRepository abstractRepository,
            StorageFileService storageFileService
    ) {
        super(abstractRepository);
        this.storageFileService = storageFileService;
    }

    public ChatMessageFile create(ChatMessage chatMessage, ChatMessageFilePostDTO chatMessageFilePostDTO) {
        StorageFile file = storageFileService.create(chatMessageFilePostDTO.getFile(), StorageFileType.CHAT_MESSAGE_FILE);
        ChatMessageFile chatMessageFile = new ChatMessageFile();
        chatMessageFile.setChatMessage(chatMessage);
        chatMessageFile.setFile(file);
        chatMessageFile.setName(chatMessageFilePostDTO.getFile().getName());
        chatMessageFile.setIsDeleted(false);
        return save(chatMessageFile);
    }
}
