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
    private String content;
    private String title;
    private Long cardId;



}
