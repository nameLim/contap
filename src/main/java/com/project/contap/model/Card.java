package com.project.contap.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Card extends TimeStamped{
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String profile;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String interest;

    @Column(nullable = false)
    private String stack;

    @Column(nullable = false)
    private Number userId;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private Number cardId;


}
