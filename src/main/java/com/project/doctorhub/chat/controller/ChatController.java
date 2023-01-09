package com.project.doctorhub.chat.controller;

import com.project.doctorhub.base.dto.HttpResponse;
import com.project.doctorhub.chat.dto.ChatMessageDTOMapper;
import com.project.doctorhub.chat.dto.ChatMessageFilePostDTO;
import com.project.doctorhub.chat.dto.ChatMessageGetDTO;
import com.project.doctorhub.chat.dto.ChatMessageSendDTO;
import com.project.doctorhub.chat.model.Chat;
import com.project.doctorhub.chat.model.ChatMessage;
import com.project.doctorhub.chat.service.ChatMessageService;
import com.project.doctorhub.chat.service.ChatService;
import com.project.doctorhub.user.model.User;
import com.project.doctorhub.user.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/chat")
public class ChatController {

    private final ChatService chatService;
    private final UserService userService;
    private final ChatMessageService chatMessageService;
    private final ChatMessageDTOMapper chatMessageDTOMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(
            @Payload ChatMessageSendDTO chatMessageSendDTO,
            Authentication authentication
    ) {
        User user = userService.findByAuthentication(authentication);
        Chat chat = chatService.findByUUIDNotDeleted(chatMessageSendDTO.getChatId());
        ChatMessage chatMessage = chatMessageService.create(user, chat, chatMessageSendDTO);
        ChatMessageGetDTO chatMessageGetDTO = chatMessageDTOMapper.entityToGetDTO(false, chatMessage);
        String destination = String.format("/chat/%s/user/%s", chatMessage.getChat().getUUID(), chatMessageSendDTO.getReceiverId());
        messagingTemplate.convertAndSend(destination, chatMessageGetDTO);
    }

    @GetMapping("/{chatId}/messages")
    public ResponseEntity<HttpResponse<Page<ChatMessageGetDTO>>> getChatMessages(
            Authentication authentication,
            @PathVariable String chatId,
            @PageableDefault Pageable pageable
    ) {
        User user = userService.findByAuthentication(authentication);
        Chat chat = chatService.findByUUIDNotDeleted(chatId);
        Page<ChatMessage> chatMessages = chatMessageService.findAllByChatNotDeleted(chat, pageable);
        Page<ChatMessageGetDTO> chatMessagesDTO = chatMessages.map(chatMessage -> chatMessageDTOMapper.entityToGetDTO(user.getUUID(), chatMessage));
        return ResponseEntity.ok(new HttpResponse<>(chatMessagesDTO));
    }

    @PostMapping("/message/file")
    public ResponseEntity<HttpResponse<ChatMessageGetDTO>> uploadChatMessage(
            Authentication authentication,
            @ModelAttribute ChatMessageFilePostDTO chatMessageFilePostDTO
    ) {
        User user = userService.findByAuthentication(authentication);
        Chat chat = chatService.findByUUIDNotDeleted(chatMessageFilePostDTO.getChatId());
        ChatMessage chatMessage = chatMessageService.create(user, chat, chatMessageFilePostDTO);
        String destination = String.format("/chat/%s/user/%s", chatMessage.getChat().getUUID(), chatMessageFilePostDTO.getReceiverId());
        messagingTemplate.convertAndSend(destination, chatMessageDTOMapper.entityToGetDTO(false, chatMessage));
        return ResponseEntity.ok(new HttpResponse<>(chatMessageDTOMapper.entityToGetDTO(true, chatMessage)));
    }

}
