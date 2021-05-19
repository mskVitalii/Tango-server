package com.tango.DTO;

// TODO  Этим классом будет общаться сервер
// TODO  мб нужно будет сделать

import com.tango.models.chat.message.Message;
import com.tango.models.chat.message.MessageType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class MessageDTO {

    long chatUserId;
    long chatId;
    String message;
    LocalDate posted;
    MessageType messageType;
}
