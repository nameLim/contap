package com.project.contap.common;

import com.project.contap.chat.ChatMessageDTO;
import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.common.enumlist.MsgTypeEnum;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.user.User;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.UUID;

@Component
public class Common {
    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;
    private final FriendRepository friendRepository;
    @Autowired
    public Common(ChatRoomRepository chatRoomRepository,
                  RedisTemplate redisTemplate,
                  ChannelTopic channelTopic,
                  FriendRepository friendRepository)
    {
        this.chatRoomRepository = chatRoomRepository;
        this.redisTemplate=redisTemplate;
        this.channelTopic = channelTopic;
        this.friendRepository = friendRepository;
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
        }
        else{
            chatRoomRepository.setAlarm(tapReceiver,type.getAlarmEnum());
//            if(type.equals(MsgTypeEnum.SEND_TAP))
//                sendSMS();
        }
    }

    public void makeChatRoom(User sendUser, User receiveUser) {
        String roomId = UUID.randomUUID().toString();
        Friend fir = Friend.builder()
                .roomId(roomId)
                .me(receiveUser)
                .you(sendUser)
                .build();
        Friend sec = Friend.builder()
                .roomId(roomId)
                .me(sendUser)
                .you(receiveUser)
                .build();
        friendRepository.save(fir);
        friendRepository.save(sec);
        chatRoomRepository.whenMakeFriend(roomId,sendUser.getEmail(),receiveUser.getEmail());
    }

    private void sendSMS() {
        String api_key = "a";
        String api_secret = "a";
        Message coolsms = new Message(api_key, api_secret);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", "01040343120");   // 탭요청 알람을 받기위해서 정확하게 기재해주세요
        // ㅎ
        params.put("from", "01066454534"); //사전에 사이트에서 번호를 인증하고 등록하여야 함 // 070 번호하나사고
        params.put("type", "SMS");
        params.put("text", "이승준 님이 탭 요청을 하였습니다..!"); //메시지 내용
        params.put("app_version", "test app 1.2");
        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString()); //전송 결과 출력
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
}
