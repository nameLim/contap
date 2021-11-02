package com.project.contap.chat.repository;

import com.project.contap.chat.model.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

}
