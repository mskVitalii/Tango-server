package com.tango.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tango.models.chat.message.MessageType;
import lombok.Data;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.time.LocalDate;
import java.util.Date;

@Data
public class MessageResponseDTO {

    private String username;
    private String avatar;
//    @Temporal(TemporalType.DATE)
@JsonFormat
        (shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date posted;
    private String message;
    private MessageType messageType;

    public MessageResponseDTO(String username,
                              String avatar,
                              Date posted,
                              String message,
                              MessageType messageType) {
        this.username = username;
        this.avatar = avatar;
        this.posted = posted;
        this.message = message;
        this.messageType = messageType;
    }
}
