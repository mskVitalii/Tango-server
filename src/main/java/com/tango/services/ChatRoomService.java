package com.tango.services;

import com.tango.DTO.ChatCreationRequest;
import com.tango.DTO.InvitationRequest;
import com.tango.exception.TangoException;
import com.tango.models.chat.attachment.Attachment;
import com.tango.models.chat.attachment.AttachmentRepository;
import com.tango.models.chat.room.ChatRoom;
import com.tango.models.chat.room.ChatRoomRepository;
import com.tango.models.chat.user.ChatUser;
import com.tango.models.chat.user.ChatUserRepository;
import com.tango.models.chat.user.ChatUserRights;
import com.tango.models.user.User;
import com.tango.utils.PictureUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatRoomService {

    final ChatRoomRepository chatRoomRepository;
    final ChatUserRepository chatUserRepository;
    final AttachmentRepository attachmentRepository;
    final UserServiceImpl userService;
    final PictureUtils pictureUtils;

    public ChatRoomService(@Autowired ChatRoomRepository chatRoomRepository,
                           @Autowired ChatUserRepository chatUserRepository,
                           @Autowired AttachmentRepository attachmentRepository,
                           @Autowired UserServiceImpl userService,
                           @Autowired PictureUtils pictureUtils) {
        this.chatRoomRepository = chatRoomRepository;
        this.chatUserRepository = chatUserRepository;
        this.attachmentRepository = attachmentRepository;
        this.userService = userService;
        this.pictureUtils = pictureUtils;
    }

    public ChatRoom createChatRoom(ChatCreationRequest chatDTO) {
        // Подтягиваем юзера
        User user = userService.findById(chatDTO.getUserId());
        ChatRoom chatRoom = chatDTO.from();

        // Установка аватара
        if (chatDTO.getAvatar() != null)
            chatRoom.setAvatar(pictureUtils.postPictureToImgur(chatDTO.getAvatar()));

        try {
            chatRoomRepository.save(chatRoom);
        } catch (Exception ignored) {
            throw new TangoException("Name must be unique");
        }

        // пользователи чатика
        ChatUser chatOwner = new ChatUser(ChatUserRights.GOD, true, user, chatRoom);
        chatUserRepository.save(chatOwner);

        List<Long> startedUsers = chatDTO.getStartedUsers();
        if (startedUsers != null) {
            for (var startedUser : startedUsers) {
                User userBD = userService.findById(startedUser);
                ChatUser chatUser = new ChatUser(ChatUserRights.USER, false, userBD, chatRoom);
                chatUserRepository.save(chatUser);
            }
        }
        return chatRoom;
    }

    public ChatRoom editChatRoom(long chatId, ChatCreationRequest chatDTO) {
        if (chatId != chatDTO.getUserId())
            throw new TangoException("User have no rights for that action");

        ChatRoom chatRoom = chatRoomRepository.getOne(chatId);
        if (chatDTO.getAvatar() != null) chatRoom.setAvatar(pictureUtils.postPictureToImgur(chatDTO.getAvatar()));
        if (chatDTO.getInfo() != null) chatRoom.setInfo(chatDTO.getInfo());
        if (chatDTO.getName() != null) chatRoom.setName(chatDTO.getName());
        chatRoom.setPrivate(chatDTO.isPrivate());
        return chatRoomRepository.save(chatRoom);
    }

    public Page<ChatRoom> getChatsByUserId(long userId, Pageable pageable) {
        return chatUserRepository.findAllByUserUserIdOrderByChatRoomLastDesc(userId, pageable);
    }

    public Page<Attachment> getAttachments(long chatId, Pageable pageable) {
        return attachmentRepository.findAllByChatIdOrderByDesc(chatId, pageable);
    }

    public ChatUser createInvitation(long chatId, InvitationRequest user) {
        // Проверка на наличие человека в беседе
        if (chatUserRepository.existsByChatAndUser(chatId, user.getUserId()))
            throw new TangoException("User already in chat " + chatId);

        // Создание приглашения
        User userDB = userService.findById(user.getUserId());
        ChatRoom chatRoom = chatRoomRepository.findById(chatId).orElseThrow(()
                -> new TangoException("chat_id = " + chatId + " does not exist"));

        ChatUserRights chatUserRights = ChatUserRights.getInstance(user.getRights());

        ChatUser chatUser = new ChatUser(chatUserRights, false, userDB, chatRoom);
        return chatUserRepository.save(chatUser);
    }

    public Page<ChatUser> getInvitation(long userId, Pageable pageable) {
        return chatUserRepository.findAllInvitations(userId, pageable);
    }

    public ChatUser acceptInvitation(long chatUserId) {
        ChatUser chatUser = chatUserRepository.getOne(chatUserId);
        chatUser.setJoined(true);
        return chatUserRepository.save(chatUser);
    }

    public ChatUser editUser(long chatUserId, InvitationRequest invitationRequest) {
        ChatUser chatUser = chatUserRepository.getOne(chatUserId);
        if (invitationRequest.getRights() != null) {
            ChatUserRights chatUserRights = ChatUserRights.getInstance(invitationRequest.getRights());
            chatUser.setRights(chatUserRights);
        }
        if (invitationRequest.getRole() != null) {
            //TODO сделать логику с ролями в чате
        }
        return chatUserRepository.save(chatUser);
    }

    public void deleteChatUserById(long chatUserId) {
        try {
            chatUserRepository.deleteById(chatUserId);
        } catch (Exception e) {
            throw new TangoException(e.getMessage());
        }
    }
}

