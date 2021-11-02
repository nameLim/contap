package com.project.contap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BackRequestCardDto {
    private String title;
    private String content;
    private String tagsStr;
    private String link;
}
