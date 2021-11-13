package com.project.contap.model.user;

import com.project.contap.common.SearchRequestDto;
import com.project.contap.model.user.dto.UserMainDto;
import com.project.contap.model.user.dto.UserTapDto;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CostomUserRepository {
    List<UserTapDto> findMysendORreceiveTapUserInfo(Long userId, int type, int page);
    List<UserTapDto> findMyFriendsById(Long userId,List<String> orderList);
    List<UserMainDto> findAllByTag(SearchRequestDto tagsandtype);
    List<UserMainDto> getRandomUser(Long usercnt);
    Boolean existUserByUserName(String userName);
    Boolean existUserByPhoneNumber(String phoneNumber);
}
