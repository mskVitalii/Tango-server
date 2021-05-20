package com.tango.controllers;

import com.tango.DTO.MessageDTO;
import com.tango.DTO.MessageResponseDTO;
import com.tango.models.chat.ChatMessage;
import com.tango.models.chat.message.Message;
import com.tango.models.chat.user.ChatUser;
import com.tango.services.ChatUserService;
import com.tango.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.ZoneId;
import java.util.Date;

@Controller
public class ChatController {

//    @MessageMapping("/chat.sendMessage")
//    @SendTo("/topic/public")
//    public ChatMessage sendMessage(@Payload ChatMessage chatMessage) {
//        return chatMessage;
//    }
    final ChatUserService chatUserService;
    final MessageService messageService;

    public ChatController(@Autowired ChatUserService chatUserService,
                          @Autowired MessageService messageService) {
        this.chatUserService = chatUserService;
        this.messageService = messageService;
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public ChatMessage addUser(@Payload ChatMessage chatMessage,
                               SimpMessageHeaderAccessor headerAccessor) {
        // Add username in web socket session
        headerAccessor.getSessionAttributes().put("username", chatMessage.getSender());
        return chatMessage;
    }

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public MessageResponseDTO sendMessage(@Payload MessageDTO messageDTO) {
        Message message = messageService.toMessageWithSave(messageDTO);
        ChatUser chatUser = message.getChatUser();

        return new MessageResponseDTO(
                message.getMessageId(),
                chatUser.getUser().getUsername(),
                chatUser.getUser().getAvatar(),
                Date.from(message.getPosted().atStartOfDay(ZoneId.systemDefault()).toInstant()),
                message.getMessage(),
                message.getMessageType());
    }
}