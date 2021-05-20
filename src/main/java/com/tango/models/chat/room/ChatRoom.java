package com.tango.models.chat.room;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tango.models.chat.attachment.Attachment;
import com.tango.models.chat.message.Message;
import com.tango.models.chat.user.ChatUser;
import com.tango.models.chat.user.ChatUserRights;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "chats")
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class ChatRoom {

    @JsonProperty(value = "chat_id")
    @Id
    @SequenceGenerator(
            name = "chat_id",
            sequenceName = "chat_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chat_id"
    )
    long chatId;

    @Column(name = "private")
    boolean isPrivate = false;

    @Column
    String avatar;

    @Column(columnDefinition = "text")
    String info;

    @Column
    String name;

    @Column(name = "last_posted")
    LocalDate lastPosted = LocalDate.now();

    public ChatRoom(String name) {
        this.name = name;
    }

    public ChatRoom(boolean isPrivate, String name) {
        this.isPrivate = isPrivate;
        this.name = name;
    }

    public ChatRoom(boolean isPrivate, String info, String name) {
        this.isPrivate = isPrivate;
        this.info = info;
        this.name = name;
    }

    //------------------------------------FOREIGN ENTITIES
//    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<Attachment> attachments = new ArrayList<>();
//
//    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<Message> messages = new ArrayList<>();
//
//    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
//    List<ChatUser> chatUsers = new ArrayList<>();

//    //////////////////////////////////// ATTACHMENTS
//    public void addAttachment(Attachment attachment) {
//        this.attachments.add(attachment);
//        attachment.setChatRoom(this);
//    }
//
//    public void removeAttachment(Attachment attachment) {
//        this.attachments.remove(attachment);
//        attachment.setChatRoom(null);
//    }
//
//    //////////////////////////////////// MESSAGES
//    public void addMessage(Message message) {
//        this.messages.add(message);
//        message.setChatRoom(this);
//    }
//
//    public void removeMessage(Message message) {
//        this.messages.remove(message);
//        message.setChatRoom(null);
//    }
//
//    //////////////////////////////////// USERS
//    public void addChatUser(ChatUser chatUser) {
//        this.chatUsers.add(chatUser);
//        chatUser.setChatRoom(this);
//    }
//
//    public void removeChatUser(ChatUser chatUser) {
//        this.chatUsers.remove(chatUser);
//        chatUser.setChatRoom(null);
//    }


    //------------------------------------UTILS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatRoom chatRoom = (ChatRoom) o;
        return chatId == chatRoom.chatId && name.equals(chatRoom.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId, name);
    }

    @Override
    public String toString() {
        return "ChatRoom{" +
                "chatId=" + chatId +
                ", isPrivate=" + isPrivate +
                ", avatar='" + avatar + '\'' +
                ", info='" + info + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
