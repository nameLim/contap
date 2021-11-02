package com.project.contap.chat.repository;

import com.project.contap.chat.model.ChatMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatMemberRepository extends JpaRepository<ChatMember, Long> {
    ChatMember findByMemberIdAndRoomId(Long memberId, String roomId);
}
