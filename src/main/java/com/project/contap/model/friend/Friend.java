package com.project.contap.model.friend;

import com.project.contap.model.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}

