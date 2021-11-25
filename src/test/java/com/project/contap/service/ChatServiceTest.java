package com.project.contap.service;

import com.project.contap.common.Common;
import com.project.contap.common.DefaultRsp;
import com.project.contap.model.chat.ChatMessage;
import com.project.contap.model.chat.ChatMessageDTO;
import com.project.contap.model.chat.ChatMessageRepository;
import com.project.contap.model.chat.ChatRoomRepository;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ChatServiceTest {
    @Mock
    RedisTemplate redisTemplate;
    @Mock
    ChatMessageRepository chatMessageRepository;
    @Mock
    ChatRoomRepository chatRoomRepository;
    @Mock
    ChannelTopic channelTopic;
    private HashMap<String, List<ChatMessage>> updateRoomList2 = new HashMap<>(); // 메모리문제가있으려나 ..?
    private List<ChatMessage> chatmessages= new ArrayList<>(); // for bulk insert
    private String subPrefix = "/sub";
    private int subPrefixlen = 15;

    @Nested
    @DisplayName("채팅 내용 불러오기")
    class GetMsg {
        @Test
        @DisplayName("채팅방 최초 입장.")
        void GetMsg() {
            String roomId = "abc";
            ChatMessageDTO message1 = new ChatMessageDTO();
            message1.setMessage("msg1");
            message1.setRoomId(roomId);
            message1.setWriter("a");
            ChatMessageDTO message2 = new ChatMessageDTO();
            message2.setMessage("msg2");
            message2.setRoomId(roomId);
            message2.setWriter("a");
            ChatMessageDTO message3 = new ChatMessageDTO();
            message3.setMessage("msg3");
            message3.setRoomId(roomId);
            message3.setWriter("a");
            ChatService chatService = new ChatService(redisTemplate,chatMessageRepository,chatRoomRepository,channelTopic);
            when(chatRoomRepository.getChatUserCnt(any()))
                    .thenReturn(2);
            chatService.publish(message1,"sender");
            chatService.publish(message2,"sender");
            chatService.publish(message3,"sender");
            List<ChatMessage> chatmsg = new ArrayList<>();
            for(Long i = 1L ; i < 4L ; i++) {
                ChatMessage msg = new ChatMessage();
                msg.setMessage(String.format("%d",i));
                msg.setWriter("a");
                msg.setRoomId(roomId);
                msg.setId(i);
                msg.setCreatedDt(LocalDateTime.now());
                chatmsg.add(msg);
            }
            when(chatMessageRepository.findMessage(roomId,0L)).thenReturn(chatmsg);
            List<ChatMessage> ret = chatService.findMessage(roomId,0L);
            System.out.println(ret);
            for(ChatMessage chatMessage : ret)
            {
                assertEquals(roomId,chatMessage.getRoomId());
            }
        }

        @Test
        @DisplayName("채팅방 페이징기능")
        void GetMsg_page() {
            String roomId = "abc";
            ChatMessageDTO message1 = new ChatMessageDTO();
            message1.setMessage("msg1");
            message1.setRoomId(roomId);
            message1.setWriter("a");
            ChatMessageDTO message2 = new ChatMessageDTO();
            message2.setMessage("msg2");
            message2.setRoomId(roomId);
            message2.setWriter("a");
            ChatMessageDTO message3 = new ChatMessageDTO();
            message3.setMessage("msg3");
            message3.setRoomId(roomId);
            message3.setWriter("a");
            ChatService chatService = new ChatService(redisTemplate,chatMessageRepository,chatRoomRepository,channelTopic);
            when(chatRoomRepository.getChatUserCnt(any()))
                    .thenReturn(2);
            chatService.publish(message1,"sender");
            chatService.publish(message2,"sender");
            chatService.publish(message3,"sender");
            List<ChatMessage> chatmsg = new ArrayList<>();
            for(Long i = 1L ; i < 4L ; i++) {
                ChatMessage msg = new ChatMessage();
                msg.setMessage(String.format("%d",i));
                msg.setWriter("a");
                msg.setRoomId(roomId);
                msg.setId(i);
                msg.setCreatedDt(LocalDateTime.now());
                chatmsg.add(msg);
            }
            when(chatMessageRepository.findMessage(roomId,1L)).thenReturn(chatmsg);
            List<ChatMessage> ret = chatService.findMessage(roomId,1L);
            System.out.println(ret);

            assertEquals(chatmsg.size(),ret.size());
            for(int i = 0; i < 3; i++)
            {
                assertEquals(chatmsg.get(i).getId(),ret.get(i).getId());
            }
        }
    }
}
