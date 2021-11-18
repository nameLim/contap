package com.project.contap.service;

import com.project.contap.chat.ChatMessage;
import com.project.contap.chat.ChatMessageDTO;
import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.common.enumlist.AlarmEnum;
import com.project.contap.common.enumlist.MsgTypeEnum;
import com.project.contap.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;


@RequiredArgsConstructor
@Service
public class ChatService {
    private final RedisTemplate redisTemplate;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatroomRepository;
    private final ChannelTopic channelTopic;
//    private List<String> updateRoomList = new ArrayList<>(); //  HashMap 으로 변경하자
    private HashMap<String,List<ChatMessage>> updateRoomList2 = new HashMap<>(); // 메모리문제가있으려나 ..?
    private List<ChatMessage> chatmessages= new ArrayList<>(); // for bulk insert
    //msg저장 방식에대해서 조금더 생각해보자 완전 바껴야할수도있을것같음.
    private String subPrefix = "/sub";
    private int subPrefixlen = 15;


    public void publish(ChatMessageDTO message,String senderName) {
        ChatMessage newmsg = new ChatMessage(message);
        message.setWriterSessionId(senderName);
        chatmessages.add(newmsg);
        if (updateRoomList2.containsKey(newmsg.getRoomId())) //있
            updateRoomList2.get(newmsg.getRoomId()).add(newmsg);
        else
            updateRoomList2.put(newmsg.getRoomId(),new ArrayList<ChatMessage>(){{add(newmsg);}});

        int inRoomUserCnt = chatroomRepository.getChatUserCnt(message.getRoomId());
        if(inRoomUserCnt == 1)
        {
            String recieverId = chatroomRepository.getSessionId(message.getReciever());
            if (recieverId == null) {
                message.setType(MsgTypeEnum.CHAT_EITHER_LOGOFF.getValue());
                chatroomRepository.setAlarm(message.getReciever(), AlarmEnum.CHAT);
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
        updateRoomList2.clear();
        chatmessages.clear();
    }

    public void userConnect(String dest,String sessionId,String userEmail,String sessionId2) {

        if (dest.startsWith(subPrefix)) {
            String roomId =  dest.substring(subPrefixlen);
            chatroomRepository.enterRoom(roomId,userEmail,sessionId2);
        }
        else
            chatroomRepository.userConnect(userEmail,sessionId);
    }
    public void userDisConnect(String userName,String sessionId) {
        chatroomRepository.userDisConnect(userName,sessionId);
    }

    public List<ChatMessage> getchatmsg(String roomId) {
        List<ChatMessage> chatList = chatMessageRepository.findAllByRoomId(roomId);
        if(updateRoomList2.containsKey(roomId))
            chatList.addAll(updateRoomList2.get(roomId));
        return chatList;
    }

    public List<ChatMessage> findMessage(String roomId, Long longId) {
        List<ChatMessage> chatList = chatMessageRepository.findMessage(roomId,longId);
        if(longId <= 0) {
            Collections.reverse(chatList);
            if (updateRoomList2.containsKey(roomId))
                chatList.addAll(updateRoomList2.get(roomId));
        }
        return chatList;
    }
}
