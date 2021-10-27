package com.project.contap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
public class Card {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    private User user;

    @Column
    private Integer cardOrder; // (1~10)

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String filePath;

    @ManyToMany
    private List<HashTag> hashTags;

    public Card(User user, Integer cardOrder, String title, String content, String filePath)
    {
        this.user = user;
        this.cardOrder = cardOrder;
        this.title = title;
        this.content = content;
        this.filePath = filePath;
    }

}
