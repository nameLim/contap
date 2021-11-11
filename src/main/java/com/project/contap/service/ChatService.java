package com.project.contap.service;

import com.project.contap.chat.ChatMessage;
import com.project.contap.chat.ChatMessageDTO;
import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.common.enumlist.MsgTypeEnum;
import com.project.contap.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ChatService {
    private final RedisTemplate redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatroomRepository;
    private final ChannelTopic channelTopic;
    private List<String> updateRoomList = new ArrayList<>(); //  check new msg
    private List<ChatMessage> chatmessages= new ArrayList<>(); // for bulk insert
    //msg저장 방식에대해서 조금더 생각해보자 완전 바껴야할수도있을것같음.
    private String subPrefix = "/sub";
    private int subPrefixlen = 15;


    public void publish(ChatMessageDTO message,String senderName) {
        ChatMessage newmsg = new ChatMessage(message);
        message.setWriterSessionId(senderName);
        chatmessages.add(newmsg);
        if(!updateRoomList.contains(newmsg.getRoomId()))
            updateRoomList.add(newmsg.getRoomId());
        int inRoomUserCnt = chatroomRepository.getChatUserCnt(message.getRoomId());
        if(inRoomUserCnt == 1)
        {
            String recieverId = chatroomRepository.getSessionId(message.getReciever());
            if (recieverId == null) {
                message.setType(MsgTypeEnum.CHAT_EITHER_LOGOFF.getValue());
                chatroomRepository.setAlarm(message.getReciever());
            }
            else {
                message.setSessionId(recieverId);
                message.setType(MsgTypeEnum.CHAT_EITHER_LOGINON.getValue());
            }
        }
        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
        chatroomRepository.newMsg(message.getRoomId(),message.getWriter(),message.getReciever(),message.getType(),message.getMessage());
        if (chatmessages.size() >= 100)
            saveBulk();
    }

    @Transactional
    public void saveBulk() {
        chatMessageRepository.saveAll(chatmessages);
        updateRoomList.clear();
        chatmessages.clear();
    }

    public void userConnect(String dest,String sessionId,String userEmail,String sessionId2) {

        if (dest.startsWith(subPrefix)) {
            String roomId =  dest.substring(subPrefixlen);
            chatroomRepository.enterRoom(roomId,userEmail,sessionId2);
            if(updateRoomList.contains(roomId))
                saveBulk();
        }
        else
            chatroomRepository.userConnect(userEmail,sessionId);
    }
    public void userDisConnect(String userName,String sessionId) {
        chatroomRepository.userDisConnect(userName,sessionId);
    }

    public List<ChatMessage> getchatmsg(String roomId) {
        return chatMessageRepository.findAllByRoomId(roomId);
    }
}
