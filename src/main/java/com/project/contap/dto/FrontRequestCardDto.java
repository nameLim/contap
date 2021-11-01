package com.project.contap.dto;

import com.project.contap.model.HashTag;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FrontRequestCardDto {
    private String profileabc; // d사용안해서 임시로 바꿔둠
    private String userName;
    private List<HashTag> hashTags;
}
