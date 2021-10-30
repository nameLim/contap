package com.project.contap.dto;

import com.project.contap.model.HashTag;
import lombok.Data;

import java.util.List;

@Data
public class myInforesDto {
    private String profile;
    private String userName;
    private List<HashTag> hashTags;
}
