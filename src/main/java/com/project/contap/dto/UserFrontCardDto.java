package com.project.contap.dto;

import com.project.contap.model.HashTag;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserFrontCardDto {
    private String profile;
    private String userName;
    private List<HashTag> stackHashTags;
}
