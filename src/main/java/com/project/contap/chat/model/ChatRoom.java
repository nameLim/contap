package com.project.contap.chat.model;

import com.project.contap.model.TimeStamped;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Getter
@Entity
@NoArgsConstructor
//@RedisHash("chatRoom")
public class ChatRoom extends TimeStamped implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String roomId;


    @Builder
    public ChatRoom(String roomId){
        this.roomId = roomId;
    }

    /*
        채팅방 생성
     */
    public static ChatRoom createChatRoom(Long challengeId){
        return ChatRoom.builder()
                .roomId(String.valueOf(challengeId))
                .build();
    }
}
