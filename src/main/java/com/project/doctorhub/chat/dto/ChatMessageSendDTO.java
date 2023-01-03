package com.project.doctorhub.chat.dto;

import com.project.doctorhub.chat.model.ChatMessageContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageSendDTO {

    private String receiverId;
    private String chatId;
    private String content;
    private ChatMessageContentType contentType;
}
