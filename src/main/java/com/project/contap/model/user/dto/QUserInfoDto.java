package com.project.contap.model.user.dto;

import com.project.contap.model.card.Card;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class QUserInfoDto {
    private Long userId;
    private String userName;
    private String profile;
    private String hashTags;
    private List<Card> Cards;

    public QUserInfoDto(
            Long userId,
            String userName,
            String profile,
            String hashTags,
            List<Card> Cards
    )
    {
        this.userId= userId;
        this.userName = userName;
        this.profile = profile;
        this.hashTags = hashTags;
        this.Cards = Cards;
    }

    public QUserInfoDto(
            Long userId,
            String userName,
            String profile,
            String hashTags
    )
    {
        this.userId= userId;
        this.userName = userName;
        this.profile = profile;
        this.hashTags = hashTags;
    }
}
