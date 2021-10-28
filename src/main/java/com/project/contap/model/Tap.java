package com.project.contap.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class Tap {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private User sendUser;

    @OneToOne(fetch = FetchType.LAZY)
    private User receiveUser;

    @Column
    private TapStatusEnum status;
}
