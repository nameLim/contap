package com.project.contap.dto;

import com.project.contap.model.HashTag;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectAddDto {
    private String title;
    private String content;
    private List<HashTag> stackHashTags;
}
