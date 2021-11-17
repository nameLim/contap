package com.project.contap.chat;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long>,CostomChatMessageRepository {
    List<ChatMessage> findAllByRoomId(String roomId);
}
