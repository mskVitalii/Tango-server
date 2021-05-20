package com.tango.controllers;

import com.tango.DTO.ChatCreationRequest;
import com.tango.DTO.InvitationRequest;
import com.tango.DTO.MessageResponseDTO;
import com.tango.DTO.PaginationResponse;
import com.tango.models.chat.attachment.Attachment;
import com.tango.models.chat.message.Message;
import com.tango.models.chat.room.ChatRoom;
import com.tango.models.chat.user.ChatUser;
import com.tango.services.ChatRoomService;
import com.tango.services.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

/**
 * Этот класс предназначен НЕ для чата,
 * а для обеспечения страницы с чатами
 * Организации приглашения юзеров итд
 */

@RestController
@RequestMapping(path = "api/chats")
@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
public class ChatsInteractionController {

    final ChatRoomService chatRoomService;
    final MessageService messageService;

    public ChatsInteractionController(@Autowired ChatRoomService chatRoomService,
                                      @Autowired MessageService messageService) {
        this.chatRoomService = chatRoomService;
        this.messageService = messageService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<?>> getChatsByUser(
            @RequestParam(name = "user_id") long userId,
            @RequestParam int page,
            @RequestParam int size) {
        Page<ChatRoom> chats = chatRoomService.getChatsByUserId(userId, PageRequest.of(page, size));
        return ResponseEntity.ok(new PaginationResponse<>(chats));
    }

    @GetMapping("attachments")
    public ResponseEntity<PaginationResponse<?>> getAttachments(
            @RequestParam(name = "chat_id") long chatId,
            @RequestParam int page,
            @RequestParam int size) {
        Page<Attachment> attachments = chatRoomService.getAttachments(chatId, PageRequest.of(page, size));
        return ResponseEntity.ok(new PaginationResponse<>(attachments));
    }

    @GetMapping("user/invitations")
    public ResponseEntity<PaginationResponse<?>> getInvitation(
            @RequestParam("user_id") long chatId,
            @RequestParam int page,
            @RequestParam int size) {
        Page<ChatUser> users = chatRoomService.getInvitation(chatId, PageRequest.of(page, size));
        return ResponseEntity.ok(new PaginationResponse<>(users));
    }

    @GetMapping("history/{chat_id}")
    public ResponseEntity<PaginationResponse<?>> getHistory(
            @PathVariable("chat_id") long chatId,
            @RequestParam int page,
            @RequestParam int size) {
        Page<MessageResponseDTO> messages = messageService.getMessagesFromChat(chatId, PageRequest.of(page, size));
        return ResponseEntity.ok(new PaginationResponse<>(messages));
    }

    @PutMapping("user/accept/{chat_user_id}")
    public ChatUser acceptInvitation(@PathVariable("chat_user_id") long chatUserId) {
        return chatRoomService.acceptInvitation(chatUserId);
    }

    @PutMapping("edit/{chat_id}")
    public ChatRoom editChat(
            @PathVariable("chat_id") long chatId,
            @RequestBody ChatCreationRequest chatDTO) {
        return chatRoomService.editChatRoom(chatId, chatDTO);
    }

    @PostMapping
    public ChatRoom createChat(@RequestBody ChatCreationRequest chatDTO) {
        return chatRoomService.createChatRoom(chatDTO);
    }

    @PostMapping("user/invite/{chat_id}")
    public ChatUser inviteUser(@PathVariable("chat_id") long chatId,
                               @RequestBody InvitationRequest invitationRequest) {
        return chatRoomService.createInvitation(chatId, invitationRequest);
    }

    @PutMapping("user/{chat_user_id}")
    public ChatUser editUser(@PathVariable("chat_user_id") long chatUserId,
                             @RequestBody InvitationRequest invitationRequest) {
        return chatRoomService.editUser(chatUserId, invitationRequest);
    }

    @DeleteMapping("user/{chat_user_id}")
    public void deleteUser(@PathVariable("chat_user_id") long chatUserId) {
        chatRoomService.deleteChatUserById(chatUserId);
    }
}
