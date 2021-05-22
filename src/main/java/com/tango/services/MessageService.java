package com.tango.services;

import com.tango.DTO.MessageDTO;
import com.tango.DTO.MessageResponseDTO;
import com.tango.models.chat.message.Message;
import com.tango.models.chat.message.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

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
        message.setChatUser(chatUserService.getChatUserByChatUserId(messageDTO.getChatUserId()));
        return messageRepository.save(message);
    }

    public Page<MessageResponseDTO> getMessagesFromChat(long chatId, Pageable pageable) {
        Page<Message> messagesByChatId = messageRepository.findAllMessagesByChatId(chatId, pageable);

        return messagesByChatId.map(message -> new MessageResponseDTO(
                message.getMessageId(),
                message.getChatUser().getUser().getUsername(),
                message.getChatUser().getUser().getAvatar(),
                message.getPosted(),
                message.getMessage(),
                message.getMessageType()));
    }
}
