package com.project.contap.model.hashtag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.project.contap.model.user.User;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
public class HashTag {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private int type; //0:stack, 1:interest

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<User> users;

}