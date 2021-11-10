package com.project.contap.model.friend;

import com.project.contap.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Friend {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    private User me;

    @ManyToOne
    private User you;

    @Column
    private String roomId;

    public Friend(User me , User you, String roomId)
    {
        this.me = me;
        this.you = you;
        this.roomId = roomId;
    }
}

