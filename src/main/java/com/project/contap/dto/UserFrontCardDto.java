package com.project.contap.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class UserFrontCardDto {
    private String profile;
    private String userName;
    private List<String> stackHashTags;
    private List<String> interestHashTags;
}
