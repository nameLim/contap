package com.project.contap.model.user.dto;

import com.project.contap.model.card.Card;
import com.project.contap.model.hashtag.HashTag;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Schema(name = "User RequestDto")
public class UserRequestDto {

    @Schema(description = "userId")
    private Long userId; // 차후에 쓰일것같아서 변수들 좀 추가했음 LSJ
    @Schema(description = "Email")
    private String email;
    @Schema(description = "비밀번호")
    private String pw;
    @Schema(description = "userName")
    private String userName;
    @Schema(description = "kakaoId")
    private Long kakaoId;
    @Schema(description = "프로필사진")
    private String profile;
    private List<HashTag> tags; // 1:스프링,2리엑트
    private String hashTags;
    private Long tapId;
    private List<Card> cards;
    private Boolean isFriend;
    private String roomId;
    private int field;
    List<List<String>> values = new ArrayList<>();

    public UserRequestDto(Long id,String email,String profile, Long kakaoId, String userName, String pw,String hashTagsString, int field) {
        this.userId = id;
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = kakaoId;
        this.profile = profile;
        this.hashTags=hashTagsString;
        this.isFriend = false;
        this.field = field;
    }
    public UserRequestDto(Long id,
                          String email,
                          String profile,
                          Long kakaoId,
                          String userName,
                          String pw,
                          String hashTagsString,
                          Long tapId) {
        this.userId = id;
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = kakaoId;
        this.profile = profile;
        this.hashTags=hashTagsString;
        this.tapId = tapId;
    }

    public UserRequestDto(Long id,
                          String email,
                          String profile,
                          Long kakaoId,
                          String userName,
                          String pw,
                          String hashTagsString,
                          String roomId) {
        this.userId = id;
        this.email = email;
        this.pw = pw;
        this.userName = userName;
        this.kakaoId = kakaoId;
        this.profile = profile;
        this.hashTags=hashTagsString;
        this.roomId = roomId;
    }

    public UserRequestDto(Long id,String profile, String userName,String hashTagsString) {
        this.userId = id;
        this.userName = userName;
        this.profile = profile;
        this.hashTags=hashTagsString;
    }

}

