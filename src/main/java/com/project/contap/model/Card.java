package com.project.contap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Card {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "User_id")
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
    private List<HashTag> Tags;


    public Card(User user, Integer cardOrder, String title, String content, String filePath)
    {
        this.user = user;
        this.cardOrder = cardOrder;
        this.title = title;
        this.content = content;
        this.filePath = filePath;
    }

}
