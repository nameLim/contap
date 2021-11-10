package com.project.contap.model.user.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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
