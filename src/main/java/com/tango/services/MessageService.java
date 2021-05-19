package com.tango.services;

import com.tango.DTO.MessageDTO;
import com.tango.models.chat.message.Message;
import com.tango.models.chat.message.MessageRepository;
import com.tango.models.chat.user.ChatUser;
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

    public Message toMessage(MessageDTO messageDTO) {
        Message message = new Message(
                messageDTO.getMessageType(),
                messageDTO.getMessage(),
                messageDTO.getPosted());
        long chatUserId;
        long chatId;
        ChatUser chatUser = chatUserService.getUserByChatId(messageDTO.getChatUserId());
    return message;
    }
}
