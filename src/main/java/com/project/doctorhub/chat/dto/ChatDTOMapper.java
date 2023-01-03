package com.project.doctorhub.chat.dto;

import com.project.doctorhub.chat.model.Chat;
import org.springframework.stereotype.Component;

@Component
public class ChatDTOMapper {

    public ChatGetDTO entityToGetDTO(Chat entity) {
        ChatGetDTO dto = new ChatGetDTO();
        dto.setId(entity.getUUID());
        return dto;
    }
}
