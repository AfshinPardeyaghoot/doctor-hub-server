package com.project.doctorhub.chat.dto;

import com.project.doctorhub.chat.model.ChatMessage;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class ChatMessageDTOMapper {

    public ChatMessageGetDTO entityToGetDTO(String fetchBy, ChatMessage entity) {
        ChatMessageGetDTO dto = new ChatMessageGetDTO();
        dto.setContent(entity.getContent());
        dto.setContentType(entity.getContentType());
        dto.setSendAt(entity.getCreatedAt().toEpochMilli());
        if (Objects.equals(entity.getSendBy().getUUID(), fetchBy))
            dto.setIsOwner(true);
        else dto.setIsOwner(false);
        return dto;
    }
}
