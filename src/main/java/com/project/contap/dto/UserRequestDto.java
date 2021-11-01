package com.project.contap.dto;

import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class UserRequestDto {
    private Long userId; // 차후에 쓰일것같아서 변수들 좀 추가했음 LSJ
    private String email;
    private String pw;
    private String userName;
    private Long kakaoId;
    private String profile;
    private List<HashTag> tags; // 1:스프링,2리엑트
    private String hashTags;
    private Long tapId;
    private List<Card> cards;
    private Boolean isFriend;

    public UserRequestDto(Long id,String email,String profile, Long kakaoId, String userName, String pw,String hashTagsString) {
        this.userId = id;
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = kakaoId;
        this.profile = profile;
        this.hashTags=hashTagsString;
        this.isFriend = false;
    }
    public UserRequestDto(Long id,String email,String profile, Long kakaoId, String userName, String pw,String hashTagsString,Long tapId) {
        this.userId = id;
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = kakaoId;
        this.profile = profile;
        this.hashTags=hashTagsString;
        this.tapId = tapId;
    }
    public UserRequestDto(Long id,String profile, String userName,String hashTagsString) {
        this.userId = id;
        this.userName = userName;
        this.profile = profile;
        this.hashTags=hashTagsString;
    }

}

