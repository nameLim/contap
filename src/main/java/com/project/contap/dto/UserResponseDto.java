package com.project.contap.dto;

import com.project.contap.model.User;
import com.project.contap.security.UserDetailsImpl;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
public class UserResponseDto {

    private Long userId;
    private String email;
    private String userName;
    private String profile;
    private LocalDateTime InsertDt;
    private String hashTags;

    public static UserResponseDto of(User user) {
        return UserResponseDto.builder()
                .email(user.getEmail())
                .userName(user.getUserName())
                .profile(user.getProfile())
                .userId(user.getId())
                .hashTags(user.getHashTagsString())
                .build();
    }

    public static List<UserResponseDto> listOf(List<User> users, UserDetailsImpl userDetails) {
        List<UserResponseDto> responseDtoList = new ArrayList<>();

        if (userDetails == null) {
            users.stream().map(UserResponseDto::of)
                    .forEach(responseDtoList::add);
        } else {
            users.stream().map(UserResponseDto::of)
                    .filter(responseDto -> !responseDto.getEmail().equals(userDetails.getUsername()))
                    .forEach(responseDtoList::add);
        }
        return responseDtoList;
    }
}