package com.project.contap.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FrontRequestCardDto {
    private String profile;
    private String userName;
    private List<String> stackHashTags;
    private List<String> interestHashTags;
}
