package com.tango.DTO;

import com.tango.models.chat.room.ChatRoom;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ChatCreationRequest {
    long userId;
    boolean isPrivate;
    String name;
    String avatar;
    String info;
    List<Long> startedUsers = new ArrayList<>();


    public ChatRoom from() {
        return new ChatRoom(isPrivate, info, name);
    }
}
