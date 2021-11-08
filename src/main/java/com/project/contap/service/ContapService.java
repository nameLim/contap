package com.project.contap.service;

import com.project.contap.chatcontroller.ChatRoomRepository;
import com.project.contap.dto.DefaultRsp;
import com.project.contap.dto.SortedFriendsDto;
import com.project.contap.dto.TagDto;
import com.project.contap.dto.UserRequestDto;
import com.project.contap.model.*;
import com.project.contap.repository.FriendRepository;
import com.project.contap.repository.TapRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class ContapService {

    private final TapRepository tapRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final FriendRepository friendRepository;
    private final ChatRoomRepository chatRoomRepository;
    @Autowired
    public  ContapService(
            TapRepository tapRepository,
            JPAQueryFactory jpaQueryFactory,
            FriendRepository friendRepository,
            ChatRoomRepository chatRoomRepository
    )
    {
        this.tapRepository = tapRepository;
        this.jpaQueryFactory =jpaQueryFactory;
        this.friendRepository = friendRepository;
        this.chatRoomRepository= chatRoomRepository;
    }

    public List<UserRequestDto> getMydoTap(User user) {
        QTap qtap = QTap.tap;
        List<UserRequestDto> abc;
        abc = jpaQueryFactory
                .select(
                        Projections.constructor(UserRequestDto.class,
                                qtap.receiveUser.id,
                                qtap.receiveUser.email,
                                qtap.receiveUser.profile,
//                                qtap.receiveUser.snsId,
                                qtap.receiveUser.userName,
                                qtap.receiveUser.pw,
                                qtap.receiveUser.hashTagsString,
                                qtap.id
                        )).distinct()
                .from(qtap)
                .where(qtap.sendUser.id.eq(user.getId()))
                .fetch();
        return abc;
    }

    public List<UserRequestDto> getMyTap(User user) {
        QTap qtap = QTap.tap;
        List<UserRequestDto> abc;
        abc = jpaQueryFactory
                .select(
                        Projections.constructor(UserRequestDto.class,
                                qtap.sendUser.id,
                                qtap.sendUser.email,
                                qtap.sendUser.profile,
//                                qtap.sendUser.kakaoId,
                                qtap.sendUser.userName,
                                qtap.sendUser.pw,
                                qtap.sendUser.hashTagsString,
                                qtap.id
                        )).distinct()
                .from(qtap)
                .where(qtap.receiveUser.id.eq(user.getId()))
                .fetch();
        return abc;
    }

    public DefaultRsp tapReject(Long tagId) {
        Tap tap = tapRepository.findById(tagId).orElse(null);
        if (tap != null)
        {
            if (tap.getStatus() !=0)
                return new DefaultRsp("이미 처리된 Tap 입니다.");
            tap.setStatus(1);
            tapRepository.save(tap);
            return new DefaultRsp("정상적으로 처리 되었습니다.");
        }
        else
        {
            return new DefaultRsp("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..");
        }
    }

    @Transactional
    public DefaultRsp rapAccept(Long tagId) {
        Tap tap = tapRepository.findById(tagId).orElse(null);
        if (tap != null)
        {
            if (tap.getStatus() !=0)
                return new DefaultRsp("이미 처리된 Tap 입니다.");
            tap.setStatus(2);
            tapRepository.save(tap);
            String roomId = UUID.randomUUID().toString();
            Friend fir = new Friend(tap.getSendUser(),tap.getReceiveUser(),roomId);
            Friend sec = new Friend(tap.getReceiveUser(),tap.getSendUser(),roomId);
            friendRepository.save(fir);
            friendRepository.save(sec);
            chatRoomRepository.whenMakeFriend(roomId,tap.getSendUser().getEmail(),tap.getReceiveUser().getEmail());
            return new DefaultRsp("정상적으로 처리 되었습니다.");
        }
        else
        {
            return new DefaultRsp("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..");
        }
    }

    public List<SortedFriendsDto>getMyfriends(User user) {
        int page = 0;


        List<List<String>> order = chatRoomRepository.getMyFriendsOrderByDate(page,user.getEmail());
        SortedFriendsDto dtoArrays[] = new SortedFriendsDto[order.get(0).size()];
        QFriend qfriend = QFriend.friend;
        Map<String, Integer> sortInfo = new HashMap<>();
        List<UserRequestDto> abc = new ArrayList<>();
        if(order.get(0).size() != 0) {
            abc = jpaQueryFactory
                    .select(
                            Projections.constructor(UserRequestDto.class,
                                    qfriend.you.id,
                                    qfriend.you.email,
                                    qfriend.you.profile,
//                                    qfriend.you.kakaoId,
                                    qfriend.you.userName,
                                    qfriend.you.pw,
                                    qfriend.you.hashTagsString,
                                    qfriend.roomId
                            )).distinct()
                    .from(qfriend)
                    .where(qfriend.me.id.eq(user.getId())
                            .and(qfriend.roomId.in(order.get(0))))
                    .fetch();
            for(int i = 0 ; i < order.get(0).size();i++)
            {
                sortInfo.put(order.get(0).get(i),i);
                dtoArrays[i] = new SortedFriendsDto();
                dtoArrays[i].setRoomStatus(order.get(1).get(i));
                dtoArrays[i].setRoomId(order.get(0).get(i));
            }
            for(UserRequestDto userDto : abc)
            {
                dtoArrays[sortInfo.get(userDto.getRoomId())].setUserId(userDto.getUserId());
                dtoArrays[sortInfo.get(userDto.getRoomId())].setEmail(userDto.getEmail());
                dtoArrays[sortInfo.get(userDto.getRoomId())].setUserName(userDto.getUserName());
                dtoArrays[sortInfo.get(userDto.getRoomId())].setProfile(userDto.getProfile());
                dtoArrays[sortInfo.get(userDto.getRoomId())].setHashTags(userDto.getHashTags());
            }
        }
        List<SortedFriendsDto> ret = Arrays.asList(dtoArrays);
        return ret;
    }
}
