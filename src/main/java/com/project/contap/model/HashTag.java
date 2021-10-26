package com.project.contap.model;

import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Entity
public class HashTag {
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column()
    private Integer type;

    public HashTag(String name, Integer type)
    {
        this.name=  name;
        this.type = type;
    }
}
