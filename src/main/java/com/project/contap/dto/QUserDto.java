package com.project.contap.dto;

import com.project.contap.model.AuthorityEnum;
import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
public class QUserDto {

    private Long id;
    private String email;
    private String pw;

    @Column(nullable = false, unique = true)
    private String userName;
    @Column(unique = true)
    private Long kakaoId;
    @Column(unique = false) // profile img path
    private String profile;
    @Column
    private String hashTagsString;







}
