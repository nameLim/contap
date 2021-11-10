package com.project.contap.model.tap;

import com.project.contap.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Entity
@Getter
@Setter
public class Tap {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User sendUser;

    @OneToOne(fetch = FetchType.LAZY)
    private User receiveUser;

    @Column
    private int status;//0 : 대기 - 1 : 거절 - 2 : 수락
    public Tap (User sendUser, User receiveUser)
    {
        this.receiveUser = receiveUser;
        this.sendUser = sendUser;
        this.status = 0;
    }
}
