package com.project.contap.service;

import com.project.contap.dto.DefaultRsp;
import com.project.contap.dto.UserRequestDto;
import com.project.contap.model.*;
import com.project.contap.repository.FriendRepository;
import com.project.contap.repository.TapRepository;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;

@Service
public class ContapService {

    private final TapRepository tapRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final FriendRepository friendRepository;
    @Autowired
    public  ContapService(
            TapRepository tapRepository,
            JPAQueryFactory jpaQueryFactory,
            FriendRepository friendRepository
    )
    {
        this.tapRepository = tapRepository;
        this.jpaQueryFactory =jpaQueryFactory;
        this.friendRepository = friendRepository;
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
                                qtap.receiveUser.kakaoId,
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
                                qtap.sendUser.kakaoId,
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
            return new DefaultRsp("정상적으로 처리 되었습니다.");
        }
        else
        {
            return new DefaultRsp("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..");
        }
    }

    public List<UserRequestDto> getMyfriends(User user) {
        QFriend qfriend = QFriend.friend;
        List<UserRequestDto> abc;
        abc = jpaQueryFactory
                .select(
                        Projections.constructor(UserRequestDto.class,
                                qfriend.you.id,
                                qfriend.you.email,
                                qfriend.you.profile,
                                qfriend.you.kakaoId,
                                qfriend.you.userName,
                                qfriend.you.pw,
                                qfriend.you.hashTagsString,
                                qfriend.roomId
                        )).distinct()
                .from(qfriend)
                .where(qfriend.me.id.eq(user.getId()))
                .fetch();
        return abc;
    }
}
