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
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column
    private Integer type;

    @ManyToMany(mappedBy = "tags")
    @JsonIgnore
    private List<User> users;

    public HashTag(String name, Integer type)
    {
        this.name=  name;
        this.type = type;
    }
    public HashTag(Long id)
    {
        this.id = id;
    }
}
