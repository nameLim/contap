package com.project.contap.dto;

import lombok.Data;

import java.util.List;

@Data
public class SearchRequestDto {
    private List<String> searchTags;
    private int type; // 0 : or / 1 : and
}
