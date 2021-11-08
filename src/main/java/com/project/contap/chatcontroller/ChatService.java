package com.project.contap.chatcontroller;

import com.project.contap.dto.ChatMessage;
import com.project.contap.dto.ChatMessageDTO;
import com.project.contap.pubsub.RedisPublisher;
import com.project.contap.repository.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ChatService {
    private final RedisTemplate redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatroomRepository;
    private final ChannelTopic channelTopic;
    private String a = "";


    private List<String> updateRoomList = new ArrayList<>(); //  check new msg
    private List<ChatMessage> chatmessages= new ArrayList<>(); // for bulk insert

    public void publish(ChatMessageDTO message,String senderName) {
        ChatMessage newmsg = new ChatMessage(message);
        message.setWriterSessionId(senderName);
        chatmessages.add(newmsg);
        if(!updateRoomList.contains(newmsg.getRoomId()))
        {
            updateRoomList.add(newmsg.getRoomId());
        }

        int inRoomUserCnt = chatroomRepository.getChatUserCnt(message.getRoomId());
        if(inRoomUserCnt == 1)
        {
            String recieverId = chatroomRepository.getSessionId(message.getReciever());
            if (recieverId == null) {
                message.setType(2);
                chatroomRepository.setAlarm(message.getReciever());
            }
            else {
                message.setSessionId(recieverId);
                message.setType(1);
            }
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);


        chatroomRepository.newMsg(message.getRoomId(),message.getWriter(),message.getReciever(),message.getType(),message.getMessage());
        if (chatmessages.size() >= 100)
        {
            saveBulk();
            updateRoomList.clear();
            chatmessages.clear();
        }
    }

    @Transactional
    public void saveBulk() {
        chatMessageRepository.saveAll(chatmessages);
    }

    public void test() {
        System.out.println("test");
    }

    public void userConnect(String dest,String sessionId,String userEmail,String sessionId2) {

        if (dest.startsWith("/sub/chat/room/")) {
            dest = dest.substring(15);
            chatroomRepository.enterRoom(dest,userEmail,sessionId2);
            if(updateRoomList.contains(dest))
            {
                saveBulk();
                updateRoomList.clear();
                chatmessages.clear();
            }
        }
        else
        {
            chatroomRepository.userConnect(userEmail,sessionId);
        }
    }
    public void userDisConnect(String userName,String sessionId) {
        chatroomRepository.userDisConnect(userName,sessionId);
    }

    public List<ChatMessage> getchatmsg(String roomId) {
        return chatMessageRepository.findAllByRoomId(roomId);
    }
}
