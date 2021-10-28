package com.project.contap.dto;

import com.project.contap.model.AuthorityEnum;
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
    private List<String> stackHashTags;
    private List<String> interestHashTags;
    private AuthorityEnum authorityEnum;
    private List<CardDto> cardDtoList;
}
