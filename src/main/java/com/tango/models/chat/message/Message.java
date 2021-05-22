package com.tango.models.chat.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tango.models.chat.attachment.Attachment;
import com.tango.models.chat.room.ChatRoom;
import com.tango.models.chat.user.ChatUser;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Message {

    @JsonProperty(value = "message_id")
    @Id
    @SequenceGenerator(
            name = "message_id",
            sequenceName = "message_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "message_id"
    )
    private long messageId;

    @Enumerated(EnumType.STRING)
    public MessageType messageType = MessageType.CHAT;

    @Column(columnDefinition = "text")
    private String message;

    @Column(nullable = false)
    private Date posted = new Date();

    //------------------------------------FOREIGN ENTITIES
    @JsonManagedReference
    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attachment> attachments = new ArrayList<>();

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.MERGE})
    private ChatUser chatUser;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    // Для создания сообщения
    public Message(String message, ChatUser chatUser, ChatRoom chatRoom) {
        this.message = message;
        this.chatUser = chatUser;
        this.chatRoom = chatRoom;
    }

    // Для отправки только вложенней
    public Message(ChatUser chatUser, ChatRoom chatRoom) {
        this.chatUser = chatUser;
        this.chatRoom = chatRoom;
    }

    // Для запросов
    public Message(MessageType messageType,
                   String message,
                   Date posted) {
        this.messageType = messageType;
        this.message = message;
        this.posted = posted;
    }


    public Attachment createAttachment(String picture,
                                       String video,
                                       String music,
                                       String map) {
        return new Attachment(picture, video, music, map, this.chatRoom, this);
    }

    //////////////////////////////////// ATTACHMENTS
    public void addAttachment(Attachment attachment) {
        this.attachments.add(attachment);
        attachment.setMessage(this);
    }

    public void removeAttachment(Attachment attachment) {
        this.attachments.remove(attachment);
        attachment.setMessage(null);
    }


    //------------------------------------UTILS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return messageId == message.messageId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageId);
    }

    @Override
    public String toString() {
        return "Message{" +
                "messageId=" + messageId +
                ", message='" + message + '\'' +
                ", posted=" + posted +
                ", messageType=" + messageType.name() +
                ", attachments=" + attachments +
//                ", user=" + "\t[" + chatUser.getChatUserId() + "] " + chatUser.getUser().getUsername() +
//                ", chatRoom=" + "\t[" + chatRoom.getChatId() + "] " + chatRoom.getName() +
                '}';
    }
}
