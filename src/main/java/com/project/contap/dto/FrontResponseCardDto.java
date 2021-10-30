package com.project.contap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FrontResponseCardDto {
    private String profile;
    private String userName;
    private String hashTagsString;
}
