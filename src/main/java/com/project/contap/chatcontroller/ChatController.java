package com.project.contap.chatcontroller;

import com.project.contap.dto.ChatMessage;
import com.project.contap.dto.ChatMessageDTO;
import com.project.contap.pubsub.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@Controller
public class ChatController {
    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void message(ChatMessageDTO message) {
        chatService.publish(message);
    }

    @PostMapping("/chat/getmsg")
    public List<ChatMessage> getmessage(@RequestBody String roomId)
    {
        return chatService.getchatmsg(roomId);// 권한체크 필수
    }

}