package com.tango.services;

import com.tango.models.chat.attachment.AttachmentRepository;
import com.tango.models.chat.message.Message;
import com.tango.models.chat.message.MessageRepository;
import com.tango.models.chat.room.ChatRoomRepository;
import com.tango.models.chat.user.ChatUser;
import com.tango.models.chat.user.ChatUserRepository;
import com.tango.utils.PictureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class ChatUserService {

    final ChatRoomRepository chatRoomRepository;
    final ChatUserRepository chatUserRepository;
    final MessageRepository messageRepository;
    final AttachmentRepository attachmentRepository;
    final PictureUtils pictureUtils;

    public ChatUserService(@Autowired ChatRoomRepository chatRoomRepository,
                           @Autowired ChatUserRepository chatUserRepository,
                           @Autowired AttachmentRepository attachmentRepository,
                           @Autowired PictureUtils pictureUtils,
                           @Autowired MessageRepository messageRepository) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatUserRepository = chatUserRepository;
        this.messageRepository = messageRepository;
        this.attachmentRepository = attachmentRepository;
        this.pictureUtils = pictureUtils;
    }

    public ChatUser getUserByChatId(long chatUserId) {
        return chatUserRepository.findByIdFetch(chatUserId).orElseThrow(()
                -> new NoSuchElementException("[ERROR] No ChatUser with id=" + chatUserId));
    }

    @Transactional
    public void deleteAllMessagesByUserId(long userId) {
        List<Message> chatUserMessages = messageRepository.getAllByUserId(userId);
        for (var message : chatUserMessages) {
            message.setChatUser(null);
            messageRepository.save(message);
        }
    }

    @Transactional
    public void deleteAllByUserId(long userId) {
        chatUserRepository.deleteAllByUserId(userId);
    }
}
