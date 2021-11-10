package com.project.contap.model.user;

import com.project.contap.model.user.dto.UserRequestDto;

import java.util.List;

public interface CostomUserRepository {
    List<UserRequestDto> findMysendORreceiveTapUserInfo(Long userId,int type);
    List<UserRequestDto> findMyFriendsById(Long userId,List<String> orderList);
}
