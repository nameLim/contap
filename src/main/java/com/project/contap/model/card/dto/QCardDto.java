package com.project.contap.model.card.dto;

import lombok.Getter;
import lombok.Setter;

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
    private Long userId;
    private int field;
    private String link;

    public QCardDto(
            Long cardId,
            String title,
            String content,
            String hashTags,
            Long userId,
            int field,
            String link

    ) {
        this.cardId = cardId;
        this.title = title;
        this.content = content;
        this.hashTags = hashTags;
        this.userId = userId;
        this.field = field;
        this.link = link;
    }
}
