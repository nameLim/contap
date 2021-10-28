package com.project.contap.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@Entity
@Setter
@Getter
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

    public HashTag(String name, int type)
    {
        this.name=  name;
        this.type = type;
    }
    public HashTag(Long id)
    {
        this.id = id;
    }
}
