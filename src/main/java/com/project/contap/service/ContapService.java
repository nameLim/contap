package com.project.contap.service;

import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.common.Common;
import com.project.contap.common.DefaultRsp;
import com.project.contap.common.enumlist.MsgTypeEnum;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.friend.SortedFriendsDto;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.UserTapDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ContapService {

    private final TapRepository tapRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final Common common;

    public List<UserTapDto> getMydoTap(User user,int page) {
        List<UserTapDto> mySendTapUserDto = userRepository.findMysendORreceiveTapUserInfo(user.getId(),0,page);
        return mySendTapUserDto;
    }

    @Transactional
    public List<UserTapDto> getMyTap(User user,int page) {

        List<UserTapDto> myReceiveTapUserDto = userRepository.findMysendORreceiveTapUserInfo(user.getId(),1,page);
        return myReceiveTapUserDto;
    }

    public DefaultRsp tapReject(Long tagId, String receiveUserEmail) {
        Tap tap = tapRepository.findById(tagId).orElse(null);
        if (tap != null)
        {
            if (tap.getStatus() !=0)
                return new DefaultRsp("이미 처리된 Tap 입니다.");
            common.sendAlarmIfneeded(MsgTypeEnum.REJECT_TAP,receiveUserEmail,tap.getSendUser().getEmail());
            tapRepository.delete(tap);
            return new DefaultRsp("정상적으로 처리 되었습니다.");
        }
        return new DefaultRsp("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..");
    }

    @Transactional
    public DefaultRsp rapAccept(Long tagId,String receiveUserEmail) {
        Tap tap = tapRepository.findById(tagId).orElse(null);
        if (tap != null)
        {
            if (tap.getStatus() !=0)
                return new DefaultRsp("이미 처리된 Tap 입니다.");
            User sendUser = tap.getSendUser();
            common.makeChatRoom(tap.getSendUser(),tap.getReceiveUser());
            common.sendAlarmIfneeded(MsgTypeEnum.ACCEPT_TAP,receiveUserEmail,sendUser.getEmail());
            tapRepository.delete(tap);
            return new DefaultRsp("정상적으로 처리 되었습니다.");
        }
        return new DefaultRsp("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..");
    }

    public List<SortedFriendsDto>getMyfriends(User user,int type) {
        List<List<String>> order = chatRoomRepository.getMyFriendsOrderByDate(0,user.getEmail(),type);
        List<UserTapDto> myFriendsUserDto = new ArrayList<>();
        List<SortedFriendsDto> ret = new ArrayList<>();

        if(order.get(0).size() != 0) {
            myFriendsUserDto = userRepository.findMyFriendsById(user.getId(),order.get(0));
            ret = sortFriendList(order,myFriendsUserDto);
        } // 페이지 기능이 사라지는게 확정이된다면 다른방법을 고려해보도록하자..

        return ret;
    }

    private List<SortedFriendsDto> sortFriendList
            (List<List<String>> order,
             List<UserTapDto> myFriendsUserDto) {
        SortedFriendsDto dtoArrays[] = new SortedFriendsDto[order.get(0).size()];
        Map<String, Integer> sortInfo = new HashMap<>();

        for(int i = 0 ; i < order.get(0).size();i++)
        {
            sortInfo.put(order.get(0).get(i),i);
            dtoArrays[i] = new SortedFriendsDto();
            dtoArrays[i].setRoomStatus(order.get(1).get(i));
            dtoArrays[i].setRoomId(order.get(0).get(i));
            String date = order.get(2).get(i);
            int e = date.indexOf("E");
            date = date.substring(0,e).replace(".","");
            dtoArrays[i].setDate(date);
        }

        for(UserTapDto userDto : myFriendsUserDto)
        {
            dtoArrays[sortInfo.get(userDto.getRoomId())].setUserId(userDto.getUserId());
            dtoArrays[sortInfo.get(userDto.getRoomId())].setEmail(userDto.getEmail());
            dtoArrays[sortInfo.get(userDto.getRoomId())].setUserName(userDto.getUserName());
            dtoArrays[sortInfo.get(userDto.getRoomId())].setProfile(userDto.getProfile());
            dtoArrays[sortInfo.get(userDto.getRoomId())].setHashTags(userDto.getHashTags());
            dtoArrays[sortInfo.get(userDto.getRoomId())].setField(userDto.getField());
        }
        return Arrays.asList(dtoArrays);
    }
}