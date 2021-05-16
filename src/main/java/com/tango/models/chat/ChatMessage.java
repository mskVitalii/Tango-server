package com.tango.models.chat;

import lombok.Data;

@Data
public class ChatMessage {
    private MessageType type;
    private String content;
    private String sender;
    public enum MessageType {
        CHAT,
        JOIN,
        LEAVE
    }
}
