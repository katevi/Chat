package com.vinnik.chat.back;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

@Controller
public class MessageController {


    @MessageMapping("/sendedMessages")
    @SendTo("/topic/publishedMessages")
    public Message greeting(@Payload Message message) throws Exception {
        return message;
    }
}
