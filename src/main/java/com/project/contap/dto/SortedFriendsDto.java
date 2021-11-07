package com.project.contap.dto;

import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class SortedFriendsDto {
    private Long userId; // 차후에 쓰일것같아서 변수들 좀 추가했음 LSJ
    private String email;
    private String userName;
    private String profile;
    private String hashTags;
    private String roomId;
    private String roomStatus;
    public SortedFriendsDto(String roomId,String roomStatus)
    {
        this.roomId = roomId;
        this.roomStatus = roomStatus;
    }
}
