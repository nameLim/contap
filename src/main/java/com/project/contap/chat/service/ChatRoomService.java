package com.project.contap.chat.service;

import com.project.contap.chat.dto.response.ChatMessageResponseDto;
import com.project.contap.chat.dto.response.ChatRoomResponseDto;
import com.project.contap.chat.model.ChatMessage;
import com.project.contap.chat.repository.ChatMessageRepository;
import com.project.contap.exception.ApiRequestException;
import com.project.contap.model.User;
import com.project.contap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
@Slf4j
public class ChatRoomService {

    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;

    // 채팅방 생성

    public ChatRoomResponseDto getEachChatRoom(String roomId, String email,int page) {

        User user = getMember(email);
        Long challengeId = Long.parseLong(roomId);

        // 페이징 처리
        Pageable pageable = PageRequest.of(page-1,15);
        Slice<ChatMessage> chatMessages = chatMessageRepository.findAllByRoomIdOrderByCreatedAtDesc(roomId,pageable);

        // slice 한 부분을 다시 sort
        List<ChatMessageResponseDto> chatMessageResponseDtoList = chatMessages.getContent().stream()
                .sorted(Comparator.comparing(chatMessage -> chatMessage.getCreatedAt()))
                .map(chatMessage -> new ChatMessageResponseDto(chatMessage))
                .collect(Collectors.toList());

        return ChatRoomResponseDto.builder()
                .chatMessages(chatMessageResponseDtoList)
                .hasNext(chatMessages.hasNext())
                .build();
    }

    private User getMember(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ApiRequestException("이메일 정보가 확인되지 않습니다."));
    }

}
