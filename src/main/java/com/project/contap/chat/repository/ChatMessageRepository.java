package com.project.contap.chat.repository;

import com.project.contap.chat.model.ChatMessage;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Slice<ChatMessage> findAllByRoomIdOrderByCreatedAtDesc(String roomId, Pageable pageable);
}
