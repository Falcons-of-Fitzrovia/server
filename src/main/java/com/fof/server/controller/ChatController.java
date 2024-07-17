package com.fof.server.controller;

import com.fof.server.model.normal.PayLoadDTO;
import com.fof.server.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ChatService chatService;

    @MessageMapping("/message")
    public PayLoadDTO sendMessage(@Payload PayLoadDTO payLoadDTO) {

        if (chatService.saveMessage(payLoadDTO)) {
            simpMessagingTemplate.convertAndSendToUser(payLoadDTO.getReceiver(), "/private", payLoadDTO);
            return payLoadDTO;
        }

        return null;
    }

}
