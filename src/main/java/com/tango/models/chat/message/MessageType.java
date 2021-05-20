package com.tango.models.chat.message;

public enum MessageType {
    CHAT,
    JOIN,
    LEAVE;

    public static MessageType fromString(String messageType) {
        return switch (messageType) {
            case "LEAVE" -> LEAVE;
            case "JOIN" -> JOIN;
            default -> CHAT;
        };
    }
}
