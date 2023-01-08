package com.project.doctorhub.chat.dto;

import com.project.doctorhub.base.config.ApplicationBaseProperties;
import com.project.doctorhub.chat.model.ChatMessage;
import com.project.doctorhub.chat.model.ChatMessageContentType;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@AllArgsConstructor
public class ChatMessageDTOMapper {

    private final ApplicationBaseProperties applicationBaseProperties;


    public ChatMessageGetDTO entityToGetDTO(String fetchBy, ChatMessage entity) {
        ChatMessageGetDTO dto = new ChatMessageGetDTO();
        if (entity.getContentType() == ChatMessageContentType.FILE || entity.getContentType() == ChatMessageContentType.IMAGE) {
            dto.setContent(applicationBaseProperties.getDownloadFileApi());
            dto.setFileName(entity.getChatMessageFile().getName());
            dto.setContent(String.format("%s/%s", applicationBaseProperties.getDownloadFileApi(), entity.getChatMessageFile().getFile().getUUID()));
        } else dto.setContent(entity.getContent());
        dto.setContentType(entity.getContentType());
        dto.setSendAt(entity.getCreatedAt().toEpochMilli());
        if (entity.getContentType() == ChatMessageContentType.FILE)
            dto.setFileName(entity.getChatMessageFile().getName());
        dto.setIsOwner(Objects.equals(entity.getSendBy().getUUID(), fetchBy));
        return dto;
    }

    public ChatMessageGetDTO entityToGetDTO(Boolean isOwner, ChatMessage entity) {
        ChatMessageGetDTO dto = new ChatMessageGetDTO();
        dto.setContentType(entity.getContentType());
        if (entity.getContentType() == ChatMessageContentType.FILE || entity.getContentType() == ChatMessageContentType.IMAGE) {
            dto.setContent(applicationBaseProperties.getDownloadFileApi());
            dto.setFileName(entity.getChatMessageFile().getName());
            dto.setContent(String.format("%s/%s", applicationBaseProperties.getDownloadFileApi(), entity.getChatMessageFile().getFile().getUUID()));
        } else dto.setContent(entity.getContent());
        dto.setSendAt(entity.getCreatedAt().toEpochMilli());
        dto.setIsOwner(isOwner);
        return dto;
    }
}
