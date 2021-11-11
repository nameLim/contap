package com.project.contap.service;

import com.project.contap.common.Common;
import com.project.contap.common.enumlist.MsgTypeEnum;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.card.QCard;
import com.project.contap.model.card.dto.QCardDto;
import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.chat.ChatMessageDTO;
import com.project.contap.common.DefaultRsp;
import com.project.contap.common.SearchRequestDto;
import com.project.contap.common.enumlist.AuthorityEnum;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.QFriend;
import com.project.contap.model.hashtag.HashTag;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.tap.QTap;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.QUser;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.model.user.dto.UserRequestDto;
import com.project.contap.common.util.RandomNumberGeneration;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class MainService {
    private final HashTagRepositoty hashTagRepositoty;
    private final UserRepository userRepository;
    private final TapRepository tapRepository;
    private final FriendRepository friendRepository;
    private final CardRepository cardRepository;
    private final UserService userService;
    private final Common common;

    @Autowired
    public MainService(
            HashTagRepositoty hashTagRepositoty,
            UserRepository userRepository,
            TapRepository tapRepository,
            FriendRepository friendRepository,
            UserService userService,
            CardRepository cardRepository,
            Common common)
    {
        this.hashTagRepositoty=hashTagRepositoty;
        this.userRepository = userRepository;
        this.tapRepository = tapRepository;
        this.friendRepository = friendRepository;
        this.userService = userService;
        this.cardRepository=cardRepository;
        this.common=common;
    }

    public List<HashTag> getHashTag() {
        return hashTagRepositoty.findAll();
    }

    public List<UserRequestDto> searchuser(SearchRequestDto tagsandtype) {
        List<UserRequestDto> ret = userRepository.findAllByTag(tagsandtype);
        return ret;
    }

    public List<QCardDto> getCards(Long userId) {
        List<QCardDto> ret =  cardRepository.findAllByUserId(userId);
        return ret;
    }

    public List<UserRequestDto> getUserDtoList(UserDetailsImpl userDetails) {
        List<UserRequestDto> ret = userRepository.getRandomUser(User.userCount);

//        if(userDetails != null) {
//            QFriend qfriend = QFriend.friend;
//            List<Long> listFriendId = jpaQueryFactory
//                    .select(
//                            qfriend.you.id
//                    ).distinct()
//                    .from(qfriend)
//                    .where(qfriend.me.id.eq(userDetails.getUser().getId()))
//                    .fetch();
//            for (UserRequestDto checkid : abc){
//                checkid.setIsFriend(listFriendId.contains(checkid.getUserId()));
//            }
//        } // 이구문은 프론트한테 한번 문의해보자

        return ret;
    }


    @Transactional
    public DefaultRsp dotap(User sendUser, Long otherUserId) {
        User receiveUser = userRepository.findById(otherUserId).orElse(null);

        Boolean checkFriend = friendRepository.checkFriend(sendUser,receiveUser);
        if (checkFriend)
            return new DefaultRsp("이미 친구 관계입니다.");

        Tap checkrecievetap = tapRepository.checkReceiveTap(receiveUser,sendUser);
        if (checkrecievetap != null)
        {
            common.makeChatRoom(sendUser,receiveUser);
            checkrecievetap.setStatus(2);
            tapRepository.save(checkrecievetap);
            return new DefaultRsp("상대방의 요청을 수락 하였습니다.");
        }

        Boolean checksendtap =tapRepository.checkSendTap(receiveUser,sendUser);
        if (checksendtap)
            return new DefaultRsp("이미 상대에게 요청을 보낸 상태입니다.");

        Tap newTap = Tap.builder()
                .sendUser(sendUser)
                .receiveUser(receiveUser)
                .build();
        tapRepository.save(newTap);
        common.sendAlarmIfneeded(MsgTypeEnum.SEND_TAP,sendUser.getEmail(),receiveUser.getEmail());
        return new DefaultRsp("정상적으로 처리 되었습니다.");
    }

    public void tutorial(int tutorialNum, User requestUser) {
        User user = userService.checkUserAuthority(requestUser);

        int authStatus = user.getAuthStatus();
        if(tutorialNum == 0) { //phone
            authStatus = authStatus| AuthorityEnum.PHONE_TUTORIAL.getAuthority();
        }
        else if(tutorialNum == 1) { // profile
            authStatus = authStatus|AuthorityEnum.PROFILE_TUTORIAL.getAuthority();
        }
        user.setAuthStatus(authStatus);
        userRepository.save(user);
    }

}