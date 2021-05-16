package com.tango.models.chat.attachment;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.tango.models.chat.message.Message;
import com.tango.models.chat.room.ChatRoom;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "attachments")
@Getter
@Setter
@NoArgsConstructor
public class Attachment {
    @JsonProperty(value = "attachment_id")
    @Id
    @SequenceGenerator(
            name = "attachment_id",
            sequenceName = "attachment_id",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "attachment_id"
    )
    private long attachmentId;

    @Column(name = "picture")
    private String picture;

    @Column(name = "video")
    private String video;

    @Column(name = "music")
    private String music;

    @Column(name = "map")
    private String map;


    public Attachment(String picture,
                      String video,
                      String music,
                      String map,
                      ChatRoom chatRoom,
                      Message message) {
        this.picture = picture;
        this.video = video;
        this.music = music;
        this.map = map;
        this.chatRoom = chatRoom;
        this.message = message;
    }

    //------------------------------------FOREIGN ENTITIES
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    private ChatRoom chatRoom;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    private Message message;

    //------------------------------------UTILS
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Attachment that = (Attachment) o;
        return attachmentId == that.attachmentId
                && Objects.equals(picture, that.picture)
                && Objects.equals(video, that.video)
                && Objects.equals(music, that.music)
                && Objects.equals(map, that.map);
    }

    @Override
    public int hashCode() {
        return Objects.hash(attachmentId, picture, video, music, map);
    }

    @Override
    public String toString() {
        var stringBuilder = new StringBuilder("Attachment{" +
                "attachmentId=" + attachmentId +
                ", message=" + "\t[" + message.getMessageId() + "] " + message.getMessage() +
                ", chatRoom=" + "\t[" + chatRoom.getChatId() + "] " + chatRoom.getName());

        if (picture != null) stringBuilder.append(", picture='").append(picture).append('\'');
        if (video != null) stringBuilder.append(", video='").append(video).append('\'');
        if (music != null) stringBuilder.append(", music='").append(music).append('\'');
        if (map != null) stringBuilder.append(", map='").append(map).append('\'');

        return stringBuilder.append('}').toString();
    }
}
