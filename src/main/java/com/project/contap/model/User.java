package com.project.contap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.contap.dto.SignUpRequestDto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter

public class User {

    // ID가 자동으로 생성 및 증가합니다.
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String pw;

    @Column(nullable = false, unique = true)
    private String userName;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true) // profile img path
    private Long profile;

    @OneToMany(mappedBy = "user")
    private List<Card> cards;

    @ManyToMany
    private List<HashTag> tags; // 1:스프링,2리엑트

    public User(String email, String pw, String userName, Long kakaoId) {
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = kakaoId;
    }

    public User(Long id,String email, String pw, String userName, Long kakaoId,Long profile) {
        this.id = id;
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = kakaoId;
        this.profile = profile;
    }

    public User(String email, String pw, String userName) {
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = null;
    }

    public User(SignUpRequestDto signUpRequestDto) {
        this.email = signUpRequestDto.getEmail();
        this.pw = signUpRequestDto.getPw();
        this.userName = signUpRequestDto.getUserName();
        this.kakaoId = null;
    }
}
