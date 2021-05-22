package com.tango.DTO;

// TODO  Этим классом будет общаться сервер
// TODO  мб нужно будет сделать

import com.tango.models.chat.message.Message;
import com.tango.models.chat.message.MessageType;
import lombok.Data;

//import java.time.LocalDate;
import java.util.Date;

@Data
public class MessageDTO {

    long chatUserId;
    long chatId;
    String message;
    String messageType;
    Date posted;

    public Message toMessage() {
        return new Message(MessageType.fromString(messageType), message, posted);
    }
}
