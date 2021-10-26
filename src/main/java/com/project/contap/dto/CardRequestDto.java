package com.project.contap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CardRequestDto {
    private Long id;
    private Number userId;
    private String profile;
    private String userName;
    private String interest;
    private String stack;
    private Number cardId;
    private String content;
    private String imageUrl;



}
