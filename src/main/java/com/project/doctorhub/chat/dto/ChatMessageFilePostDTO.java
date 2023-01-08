package com.project.doctorhub.chat.dto;

import com.project.doctorhub.chat.model.ChatMessageContentType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageFilePostDTO {

    private String chatId;
    private String receiverId;
    private MultipartFile file;
    private ChatMessageContentType contentType;

    @Override
    public String toString() {
        return "ChatMessageFilePostDTO{" +
                "chatId='" + chatId + '\'' +
                ", receiverId='" + receiverId + '\'' +
                ", file=" + file +
                ", contentType=" + contentType +
                '}';
    }
}
