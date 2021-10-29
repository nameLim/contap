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

    @ManyToMany(cascade = CascadeType.ALL)
    private List<HashTag> tags;


    public Card(User user, Long cardOrder, String title, String content, String filePath)
    {
        this.user = user;
        this.cardOrder = cardOrder;
        this.title = title;
        this.content = content;
        this.filePath = filePath;
    }

    public boolean isWritedBy(User user) {
        return this.user.getEmail().equals(user.getEmail());
    }
    public List<HashTag> addHashTag(HashTag tag) {
        this.tags.add(tag);
        return this.tags;
    }
    public void update(BackRequestCardDto backRequestCardDto, List<HashTag> tags, String tagString) {
        this.title = backRequestCardDto.getTitle();
        this.content = backRequestCardDto.getContent();
        this.hashTagsString = tagString;
        this.tags = tags;
    }
}
