package com.project.contap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.contap.dto.BackRequestCardDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Card extends TimeStamped{
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
    private String filePath;

    @Column
    private String hashTagsString;

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
    public List<HashTag> addHashTag(HashTag tag) {
        this.tags.add(tag);
        return this.tags;
    }

    public void update(BackRequestCardDto backRequestCardDto, String tagString) {
        this.title = backRequestCardDto.getTitle();
        this.content = backRequestCardDto.getContent();
        this.tags = backRequestCardDto.getHashTags();
        this.hashTagsString = tagString;
    }
}
