package com.project.contap.chat.service;

import com.project.contap.chat.dto.request.ChatMessageRequestDto;
import com.project.contap.chat.model.ChatMember;
import com.project.contap.chat.model.ChatMessage;
import com.project.contap.chat.repository.ChatMemberRepository;
import com.project.contap.chat.repository.ChatMessageRepository;
import com.project.contap.exception.ApiRequestException;
import com.project.contap.repository.UserRepository;

import com.project.contap.model.User;
import com.project.contap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final ChannelTopic channelTopic;
    private final RedisTemplate redisTemplate;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMemberRepository chatMemberRepository;

    /*
        tcp
        destination정보에서 roomId 추출
     */
    public String getRoomId(Object destination) {
        String destinationToString = String.valueOf(destination);

        int lastIndex = destinationToString.lastIndexOf('/');
        if (lastIndex != -1)
            return destinationToString.substring(lastIndex + 1);
        else
            return null;
    }

    /*
        채팅방에 메세지 발송
     */
    /*
        type이 talk일 때의 메서드
     */
    @Transactional
    public void pubTalkMessage(ChatMessageRequestDto requestDto) {
        User user = getMember(requestDto);
        ChatMessage chatMessage = ChatMessage.createTALKMessage(requestDto);
        pubMessage(chatMessage);
    }

    /*
       type이 enter일 때의 메서드
     */
    @Transactional
    public void pubEnterMessage(ChatMessageRequestDto requestDto) {
        User user = getMember(requestDto);
        // 채팅방 최초 접근자임을 확인
        ChatMember chatMember = chatMemberRepository.findByMemberIdAndRoomId(user.getId(), requestDto.getRoomId());
        // 최초 접근자의 경우 알림 메세지 발송
        if(chatMember.isStatusFirst()) {
            ChatMessage chatMessage = ChatMessage.createENTERMessage(requestDto);
            pubMessage(chatMessage);
            chatMember.setStatusFirstFalse();
        }
    }

    /*
        sub 하고 있는 이용자에게 메세지 pub
     */
    public void pubMessage(ChatMessage chatMessage){
        chatMessageRepository.save(chatMessage);
        redisTemplate.convertAndSend(channelTopic.getTopic(), chatMessage);
    }

    /*
        회원 조회
     */
    private User getMember(ChatMessageRequestDto requestDto) {
        return userRepository.findByEmail(requestDto.getNickname())
                .orElseThrow(() -> new ApiRequestException("조회되지 않는 회원입니다."));
    }


}