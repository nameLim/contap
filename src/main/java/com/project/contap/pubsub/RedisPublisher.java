package com.project.contap.pubsub;


import com.project.contap.dto.ChatMessage;
import com.project.contap.dto.ChatMessageDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
//@Service
public class RedisPublisher {
//    private final RedisTemplate<String, Object> redisTemplate;
//
//    public void publish(ChannelTopic topic, ChatMessageDTO message) {
//        redisTemplate.convertAndSend(topic.getTopic(), message);
//    }
}
