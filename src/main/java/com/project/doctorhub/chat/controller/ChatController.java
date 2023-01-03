package com.project.doctorhub.chat.controller;

import com.project.doctorhub.chat.model.ChatMessageSendDTO;
import com.project.doctorhub.user.model.User;
import com.project.doctorhub.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;


@Controller
@AllArgsConstructor
public class ChatController {

    private final UserService userService;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload ChatMessageSendDTO chatMessage,
                            Authentication authentication) {


        User user = userService.findByAuthentication(authentication);
        chatMessage.setIsOwner(false);
        messagingTemplate.convertAndSend("/consultation/" + chatMessage.getConsultationId() + "/user/" + chatMessage.getReceiverId() , chatMessage);
    }
}
