package com.project.contap.dto;

import com.project.contap.model.HashTag;
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
    private List<HashTag> hashTags;
}
