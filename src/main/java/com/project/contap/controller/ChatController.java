package com.project.contap.controller;

import com.project.contap.chat.ChatMessage;
import com.project.contap.chat.ChatMessageDTO;
import com.project.contap.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(SimpMessageHeaderAccessor headerAccessor,ChatMessageDTO message) {
        chatService.publish(message,headerAccessor.getUser().getName());
    }



    @ResponseBody
    @GetMapping("/chat/getmsg/{roomId}")
    public List<ChatMessage> getmessage(@PathVariable String roomId)
    {
        return chatService.getchatmsg(roomId);// 권한체크 필수
    }
}