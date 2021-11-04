package com.project.contap.config.handler;


import com.project.contap.chatcontroller.ChatRoomRepository;
import com.project.contap.chatcontroller.ChatService;
import com.project.contap.dto.ChatMessage;
import com.project.contap.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.messaging.SessionConnectedEvent;

import java.security.Principal;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class StompHandler implements ChannelInterceptor  {

    // websocket을 통해 들어온 요청이 처리 되기전 실행된다.
    private final ChatService chatService;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) { // websocket 연결요청
            System.out.println("connect");
        } else if (StompCommand.SUBSCRIBE == accessor.getCommand()) { // 채팅룸 구독요청
            chatService.userConnect(accessor.getDestination(),accessor.getUser().getName(),accessor.getNativeHeader("userEmail").get(0),accessor.getSessionId());
        } else if (StompCommand.DISCONNECT == accessor.getCommand()) { // Websocket 연결 종료
            chatService.userDisConnect(accessor.getUser().getName());
        }
        return message;
    }

//    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
//    }
}
