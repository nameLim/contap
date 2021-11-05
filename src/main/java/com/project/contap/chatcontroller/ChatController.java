package com.project.contap.chatcontroller;

import com.project.contap.dto.ChatMessage;
import com.project.contap.dto.ChatMessageDTO;
import com.project.contap.pubsub.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
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