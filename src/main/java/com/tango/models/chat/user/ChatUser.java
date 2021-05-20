package com.tango.models.chat.user;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tango.models.chat.room.ChatRoom;
import com.tango.models.roles.Role;
import com.tango.models.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "chat_user")
@Getter
@Setter
@NoArgsConstructor
public class ChatUser {

    @JsonProperty(value = "chat_user_id")
    @Id
    @SequenceGenerator(
            name = "chat_user_id",
            sequenceName = "chat_user_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chat_user_id"
    )
    long chatUserId;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    ChatUserRights rights = ChatUserRights.USER;

    // TODO: для новых юзеров устанавливать True
    @Column
    boolean joined = false;

    // Приглошение юзера
    public ChatUser(User user, ChatRoom chatRoom) {
        this.user = user;
        this.chatRoom = chatRoom;
    }

    // Для создателя чата
    public ChatUser(ChatUserRights rights, boolean joined, User user, ChatRoom chatRoom) {
        this.rights = rights;
        this.joined = joined;
        this.user = user;
        this.chatRoom = chatRoom;
    }

    // Приглошение с ролью
    public ChatUser(ChatUserRights rights, User user, ChatRoom chatRoom) {
        this.rights = rights;
        this.user = user;
        this.chatRoom = chatRoom;
    }

    //------------------------------------FOREIGN ENTITIES
    @ManyToOne(fetch = FetchType.LAZY)
    Role role;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    User user;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    ChatRoom chatRoom;

    //------------------------------------UTILS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChatUser chatUser = (ChatUser) o;
        return chatUserId == chatUser.chatUserId && rights == chatUser.rights && user.equals(chatUser.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatUserId, rights, user);
    }

    @Override
    public String toString() {
        return "ChatUser{" +
                "chatUserId=" + chatUserId +
                ", rights=" + rights +
                ", role=" + role.getRoleName() +
                ", user=" + "\t[" + user.getId() + "] " + user.getUsername() +
                ", chatRoom=" + "\t[" + chatRoom.getChatId() + "] " + chatRoom.getName() +
                '}';
    }
}
