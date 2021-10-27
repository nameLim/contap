package com.project.contap.dto;

import lombok.Builder;

import java.util.List;

@Builder
public class CardDto {
    private Long cardId;
    private String title;
    private String content;
    private List<String> stackHashTags;
    private List<String> interestHashTags;
    private String hashTags;
}
