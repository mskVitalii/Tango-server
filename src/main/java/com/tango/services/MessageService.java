package com.tango.services;

import com.tango.DTO.MessageDTO;
import com.tango.models.chat.message.Message;
import com.tango.models.chat.message.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    final MessageRepository messageRepository;
    final ChatRoomService chatRoomService;
    final ChatUserService chatUserService;

    public MessageService(@Autowired MessageRepository messageRepository,
                          @Autowired ChatRoomService chatRoomService,
                          @Autowired ChatUserService chatUserService) {
        this.messageRepository = messageRepository;
        this.chatRoomService = chatRoomService;
        this.chatUserService = chatUserService;
    }

    public Message toMessageWithSave(MessageDTO messageDTO) {
        Message message = messageDTO.toMessage();
        message.setChatRoom(chatRoomService.getChatByChatId(messageDTO.getChatId()));
        message.setChatUser(chatUserService.getUserByChatId(messageDTO.getChatUserId()));
        return messageRepository.save(message);
    }
}
