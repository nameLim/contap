package com.project.contap.model.user.dto;

import com.project.contap.security.UserDetailsImpl;
import com.project.contap.model.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Schema(name = "User ResonseDto",description ="userId,Email,userName,프로필 사진,시간,HashTag,PhonNumber")
public class UserResponseDto {

    @Schema(description = "userId")
    private Long userId;
    @Schema(description = "Email")
    private String email;
    @Schema(description = "userName")
    private String userName;
    @Schema(description = "프로필 사진")
    private String profile;
    @Schema(description = "시간")
    private LocalDateTime InsertDt;
    @Schema(description = "HashTag")
    private String hashTags;
    @Schema(description = "PhonNumber")
    private String phoneNumber;

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