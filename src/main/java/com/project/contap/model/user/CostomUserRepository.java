package com.project.contap.model.user;

import com.project.contap.common.SearchRequestDto;
import com.project.contap.model.user.dto.UserRequestDto;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CostomUserRepository {
    List<UserRequestDto> findMysendORreceiveTapUserInfo(Long userId,int type);
    List<UserRequestDto> findMyFriendsById(Long userId,List<String> orderList);
    List<UserRequestDto> findAllByTag(SearchRequestDto tagsandtype);
    List<UserRequestDto> getRandomUser(Long usercnt);
    Boolean existUserByUserName(String userName);
    Boolean existUserByPhoneNumber(String phoneNumber);
}
