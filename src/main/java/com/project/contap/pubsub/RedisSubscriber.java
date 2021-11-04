package com.project.contap.pubsub;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.contap.dto.ChatMessage;
import com.project.contap.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber  {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    public void sendMessage(String publishMessage) {
        try {
            // ChatMessage 객채로 맵핑
            ChatMessageDTO chatMessage = objectMapper.readValue(publishMessage, ChatMessageDTO.class);
            // 채팅방을 구독한 클라이언트에게 메시지 발송
            if(chatMessage.getType() == 1)
                messagingTemplate.convertAndSendToUser(chatMessage.getSessionId(),"/sub/user",chatMessage);
            else if (chatMessage.getType() == 0)
                messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getRoomId(), chatMessage);

        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }

    /**
     * Redis에서 메시지가 발행(publish)되면 대기하고 있던 onMessage가 해당 메시지를 받아 처리한다.
     */
//    @Override
//    public void onMessage(Message message, byte[] pattern) {
//        try {
//            // redis에서 발행된 데이터를 받아 deserialize
//            String publishMessage = (String) redisTemplate.getStringSerializer().deserialize(message.getBody());
//            // ChatMessage 객채로 맵핑
//            ChatMessageDTO roomMessage = objectMapper.readValue(publishMessage, ChatMessageDTO.class);
//            // Websocket 구독자에게 채팅 메시지 Send
//            messagingTemplate.convertAndSend("/sub/chat/room/" + roomMessage.getRoomId(), roomMessage);
//        } catch (Exception e) {
//            log.error(e.getMessage());
//        }
//    }
}
