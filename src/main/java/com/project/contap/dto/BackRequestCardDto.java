package com.project.contap.dto;

import com.project.contap.model.HashTag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BackRequestCardDto {
    private String title;
    private String content;
    private List<HashTag> hashTags;
}
