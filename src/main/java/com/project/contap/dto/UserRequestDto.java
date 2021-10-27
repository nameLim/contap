package com.project.contap.dto;

import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {
    private Long id;
    private String email;
    private String pw;
    private String userName;
    private Long kakaoId;
    private Long profile;
    private List<HashTag> tags; // 1:스프링,2리엑트

    public UserRequestDto(Long id,String email,Long profile, Long kakaoId, String userName, String pw) {
        this.id = id;
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = kakaoId;
        this.profile = profile;
    }
    public UserRequestDto(Long id,String email,Long profile, Long kakaoId, String userName, String pw,List<HashTag> tags) {
        this.id = id;
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = kakaoId;
        this.profile = profile;
        this.tags = tags;
    }
}

