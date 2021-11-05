package com.project.contap.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
@NoArgsConstructor
public class ChatMessage {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column // 차후에 채팅방과 ManyToOne관계로..
    private String roomId;

    @Column
    private String message;

    @Column
    private String writer;

    @Column
    private LocalDateTime createdDt;

    public ChatMessage(ChatMessageDTO requestdto)
    {
        this.roomId = requestdto.getRoomId();
        this.message = requestdto.getMessage();
        this.writer = requestdto.getWriter();
        this.createdDt = LocalDateTime.now();
    }
}
