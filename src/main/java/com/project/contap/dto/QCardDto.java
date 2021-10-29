package com.project.contap.dto;

import lombok.*;

import java.util.List;


@Getter
@Setter
public class QCardDto {
    private Long cardId;
    private String title;
    private String content;
    private List<String> stackHashTags;
    private List<String> interestHashTags;
    private String hashTags;

    public QCardDto(
            Long cardId,
            String title,
            String content,
            String hashTags
    ) {
        this.cardId = cardId;
        this.title = title;
        this.content = content;
        this.hashTags = hashTags;
    }
}
