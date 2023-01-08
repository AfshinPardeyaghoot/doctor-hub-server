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
public class ChatMessageGetDTO {

    private Boolean isOwner;
    private String fileName;
    private String content;
    private ChatMessageContentType contentType;
    private Long sendAt;

}
