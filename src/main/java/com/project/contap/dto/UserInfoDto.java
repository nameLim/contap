package com.project.contap.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class UserInfoDto {
    private Long userId;
    private String password;
    private String userName;
    private String profile;
    private int field;
    private int authStatus;
    private String hashTagsString;
    private List<BackResponseCardDto> cardDtoList;
}
