package com.project.contap.chatcontroller;

import com.project.contap.dto.ChatMessage;
import com.project.contap.pubsub.RedisPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;


@RequiredArgsConstructor
@Controller
public class ChatController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomRepository chatRoomRepository;

    @MessageMapping("/chat/message")
    public void message(ChatMessage message) {
        chatRoomRepository.enterChatRoom(message.getRoomId());
        ChannelTopic topic =  chatRoomRepository.gettopic(message.getRoomId());
        redisPublisher.publish(topic, message);
    }
}