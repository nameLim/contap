package com.project.contap.model.card;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.contap.model.card.dto.BackRequestCardDto;
import com.project.contap.model.hashtag.HashTag;
import com.project.contap.common.util.TimeStamped;
import com.project.contap.model.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Card extends TimeStamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @Column
    private Long cardOrder; // (1~10)

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String link;

    @Column
    private String filePath;

    @Column
    private String tagsString;

    @ManyToMany
    private List<HashTag> tags;

    public Card(User user, Long cardOrder, String title, String content){
        this.user = user;
        this.cardOrder = cardOrder;
        this.title = title;
        this.content = content;
    }

    public Card(User user, Long cardOrder, String title, String content,List<HashTag> tags) {
        this.user = user;
        this.cardOrder = cardOrder;
        this.title = title;
        this.content = content;
        this.tags = tags;
    }

    public boolean isWritedBy(User user) {
        return this.user.equals(user);
    }

    public void update(BackRequestCardDto backRequestCardDto) {
        this.title = backRequestCardDto.getTitle();
        this.content = backRequestCardDto.getContent();
        this.tags = null;
        this.tagsString = backRequestCardDto.getTagsStr();
        this.link = backRequestCardDto.getLink();
    }
}
