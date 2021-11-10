package com.project.contap.common;

import com.project.contap.chat.ChatMessageDTO;
import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.common.enumlist.MsgTypeEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

@Component
public class Common {
    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    @Autowired
    public Common(ChatRoomRepository chatRoomRepository,
                  RedisTemplate redisTemplate,
                  ChannelTopic channelTopic)
    {
        this.chatRoomRepository = chatRoomRepository;
        this.redisTemplate=redisTemplate;
        this.channelTopic = channelTopic;
    }

    //진짜 잡다한 기능들만 들어갈 예정인 클래스..
    private ChatMessageDTO setChatMessageDTO(MsgTypeEnum type,String receiver, String sender,String receiverSession)
    {
        ChatMessageDTO msg = new ChatMessageDTO();
        msg.setType(type.getValue());
        msg.setReciever(receiver);
        msg.setWriter(sender);
        msg.setMessage(type.getMsg());
        msg.setSessionId(receiverSession);
        return msg;
    }

    public void sendAlarmIfneeded(MsgTypeEnum type, String tapSender, String tapReceiver) {
        String receiverssesion = chatRoomRepository.getSessionId(tapSender);
        if(receiverssesion != null) {
            ChatMessageDTO msg = setChatMessageDTO(type,tapReceiver,tapSender,receiverssesion);
            redisTemplate.convertAndSend(channelTopic.getTopic(), msg);
        } else{
            System.out.println("로그아웃 상태에서 알람을따로 줘야하면 여기에 구현해주세요 ㅎ");
        }
    }
}
