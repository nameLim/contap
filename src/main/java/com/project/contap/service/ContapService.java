package com.project.contap.service;

import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.chat.ChatMessageDTO;
import com.project.contap.common.Common;
import com.project.contap.common.DefaultRsp;
import com.project.contap.common.enumlist.MsgTypeEnum;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.QFriend;
import com.project.contap.model.friend.SortedFriendsDto;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.tap.QTap;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.UserRequestDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ContapService {

    private final TapRepository tapRepository;
    private final FriendRepository friendRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final Common common;
    @Autowired
    public  ContapService(
            TapRepository tapRepository,
            FriendRepository friendRepository,
            ChatRoomRepository chatRoomRepository,
            UserRepository userRepository,
            Common common
    )
    {
        this.tapRepository = tapRepository;
        this.friendRepository = friendRepository;
        this.chatRoomRepository= chatRoomRepository;
        this.userRepository = userRepository;
        this.common = common;
    }

    public List<UserRequestDto> getMydoTap(User user) {
        List<UserRequestDto> mySendTapUserDto = userRepository.findMysendORreceiveTapUserInfo(user.getId(),0);
        return mySendTapUserDto;
    }

    public List<UserRequestDto> getMyTap(User user) {

        List<UserRequestDto> myReceiveTapUserDto = userRepository.findMysendORreceiveTapUserInfo(user.getId(),1);;
        return myReceiveTapUserDto;
    }

    public DefaultRsp tapReject(Long tagId, String receiveUserEmail) {
        Tap tap = tapRepository.findById(tagId).orElse(null);
        if (tap != null)
        {
            if (tap.getStatus() !=0)
                return new DefaultRsp("이미 처리된 Tap 입니다.");
            tap.setStatus(1);
            tapRepository.save(tap);

            common.sendAlarmIfneeded(MsgTypeEnum.REJECT_TAP,tap.getSendUser().getEmail(),receiveUserEmail);

            return new DefaultRsp("정상적으로 처리 되었습니다.");
        }
        return new DefaultRsp("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..");
    }

    @Transactional
    public DefaultRsp rapAccept(Long tagId,String receiveUserEmail) {
        Tap tap = tapRepository.findById(tagId).orElse(null);
        User sendUser = tap.getSendUser();
        if (tap != null)
        {
            if (tap.getStatus() !=0)
                return new DefaultRsp("이미 처리된 Tap 입니다.");
            tap.setStatus(2);
            tapRepository.save(tap);

            makeChatRoom(tap.getSendUser(),tap.getReceiveUser());

            common.sendAlarmIfneeded(MsgTypeEnum.ACCEPT_TAP,sendUser.getEmail(),receiveUserEmail);

            return new DefaultRsp("정상적으로 처리 되었습니다.");
        }
        return new DefaultRsp("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..");
    }



    public List<SortedFriendsDto>getMyfriends(User user) {
        List<List<String>> order = chatRoomRepository.getMyFriendsOrderByDate(0,user.getEmail());
        List<UserRequestDto> myFriendsUserDto = new ArrayList<>();
        List<SortedFriendsDto> ret = new ArrayList<>();

        if(order.get(0).size() != 0) {
            myFriendsUserDto = userRepository.findMyFriendsById(user.getId(),order.get(0));
            ret = sortFriendList(order,myFriendsUserDto);
        } // 페이지 기능이 사라지는게 확정이된다면 다른방법을 고려해보도록하자..

        return ret;
    }


    private void makeChatRoom(User sendUser, User receiveUser) {
        String roomId = UUID.randomUUID().toString();
        Friend fir = new Friend(sendUser,receiveUser,roomId);
        Friend sec = new Friend(receiveUser,sendUser,roomId);
        friendRepository.save(fir);
        friendRepository.save(sec);
        chatRoomRepository.whenMakeFriend(roomId,sendUser.getEmail(),receiveUser.getEmail());
    }


    private List<SortedFriendsDto> sortFriendList
            (List<List<String>> order,
             List<UserRequestDto> myFriendsUserDto) {
        SortedFriendsDto dtoArrays[] = new SortedFriendsDto[order.get(0).size()];
        Map<String, Integer> sortInfo = new HashMap<>();

        for(int i = 0 ; i < order.get(0).size();i++)
        {
            sortInfo.put(order.get(0).get(i),i);
            dtoArrays[i] = new SortedFriendsDto();
            dtoArrays[i].setRoomStatus(order.get(1).get(i));
            dtoArrays[i].setRoomId(order.get(0).get(i));
        }
        for(UserRequestDto userDto : myFriendsUserDto)
        {
            dtoArrays[sortInfo.get(userDto.getRoomId())].setUserId(userDto.getUserId());
            dtoArrays[sortInfo.get(userDto.getRoomId())].setEmail(userDto.getEmail());
            dtoArrays[sortInfo.get(userDto.getRoomId())].setUserName(userDto.getUserName());
            dtoArrays[sortInfo.get(userDto.getRoomId())].setProfile(userDto.getProfile());
            dtoArrays[sortInfo.get(userDto.getRoomId())].setHashTags(userDto.getHashTags());
        }
        return Arrays.asList(dtoArrays);
    }
}