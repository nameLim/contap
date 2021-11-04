package com.project.contap.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
public class FrontResponseCardDto{
    private String profile;
    private String userName;
    private String hashTagsString;
    private int field;
}
