package com.project.contap.service;

import com.project.contap.dto.DefaultRsp;
import com.project.contap.dto.QCardDto;
import com.project.contap.dto.SearchRequestDto;
import com.project.contap.dto.UserRequestDto;
import com.project.contap.model.*;
import com.project.contap.repository.FriendRepository;
import com.project.contap.repository.HashTagRepositoty;
import com.project.contap.repository.TapRepository;
import com.project.contap.repository.UserRepository;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.util.GetRandom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import net.nurigo.java_sdk.Coolsms;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@Service
public class MainService {
    private final HashTagRepositoty hashTagRepositoty;
    private final UserRepository userRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final TapRepository tapRepository;
    private final FriendRepository friendRepository;

    @Autowired
    public MainService(
            HashTagRepositoty hashTagRepositoty,
            JPAQueryFactory jpaQueryFactory,
            UserRepository userRepository,
            TapRepository tapRepository,
            FriendRepository friendRepository)
    {
        this.hashTagRepositoty=hashTagRepositoty;
        this.jpaQueryFactory =jpaQueryFactory;
        this.userRepository = userRepository;
        this.tapRepository = tapRepository;
        this.friendRepository = friendRepository;
    }

    public List<HashTag> getHashTag() {
        return hashTagRepositoty.findAll();
    }

    public List<UserRequestDto> fortestsearchuser() {
        QUser hu = QUser.user;
        List<Long> ids2 = Arrays.asList(new Long(GetRandom.randomRange(1,10)),new Long(GetRandom.randomRange(1,10)),new Long(GetRandom.randomRange(1,10)));
        List<UserRequestDto> abc;
        abc = jpaQueryFactory
                .select(
                        Projections.constructor(UserRequestDto.class,
                                hu.id,
                                hu.email,
                                hu.profile,
                                hu.kakaoId,
                                hu.userName,
                                hu.pw,
                                hu.hashTagsString,
                                hu.field
                        )).distinct()
                .from(hu)
                .where(hu.tags.any().id.in(ids2))
                .offset(9).limit(9)
                .fetch();
        return abc;
    }
    public List<UserRequestDto> searchuser(SearchRequestDto tagsandtype) {
        BooleanBuilder builder = new BooleanBuilder();
        int page = 9*tagsandtype.getPage();
        QUser hu = QUser.user;
        if (tagsandtype.getType() == 0) {
            for (String tagna : tagsandtype.getSearchTags()) {
                builder.or(hu.hashTagsString.contains("@"+tagna+"@"));
            }
        }
        else
        {
            for (String tagna : tagsandtype.getSearchTags()) {
                builder.and(hu.hashTagsString.contains("@"+tagna+"@"));
            }
        }
        if(tagsandtype.getField() != 3)
        {
            builder.and(hu.field.eq(tagsandtype.getField()));
        }
        List<UserRequestDto> abc;
        abc = jpaQueryFactory
                .select(
                        Projections.constructor(UserRequestDto.class,
                                hu.id,
                                hu.email,
                                hu.profile,
                                hu.kakaoId,
                                hu.userName,
                                hu.pw,
                                hu.hashTagsString,
                                hu.field
                        )).distinct()
                .from(hu)
                .where(builder)
                .offset(page).limit(9)
                .fetch();
        return abc;
    }
    public List<QCardDto> getCards(Long userId) {
        QCard hu = QCard.card;
        List<QCardDto> abc =  jpaQueryFactory
                .select(
                        Projections.constructor(QCardDto.class,
                                hu.id,
                                hu.title,
                                hu.content,
                                hu.tagsString,
                                hu.user.id,
                                hu.user.field
                        )
                )
                .from(hu)
                .where(hu.user.id.eq(userId))
                .fetch();
        return abc;
    }

    public List<UserRequestDto> getUserDtoList(UserDetailsImpl userDetails) {
        Random random = new Random();
        int page = 0;//random.nextInt(0);

        QUser hu = QUser.user;

        List<UserRequestDto> abc;
        abc = jpaQueryFactory
                .select(
                        Projections.constructor(UserRequestDto.class,
                                hu.id,
                                hu.email,
                                hu.profile,
                                hu.kakaoId,
                                hu.userName,
                                hu.pw,
                                hu.hashTagsString,
                                hu.field
                        )).distinct()
                .from(hu)
                .offset(page).limit(9)
                .fetch();

        if(userDetails != null) {
            QFriend qfriend = QFriend.friend;
            List<Long> listFriendId = jpaQueryFactory
                    .select(
                            qfriend.you.id
                    ).distinct()
                    .from(qfriend)
                    .where(qfriend.me.id.eq(userDetails.getUser().getId()))
                    .fetch();
            for (UserRequestDto checkid : abc){
                checkid.setIsFriend(listFriendId.contains(checkid.getUserId()));
            }
        }
        return abc;
    }


    @Transactional
    public DefaultRsp dotap(User sendUser, Long otherUserId) {
        User receiveUser = new User(otherUserId);
        //======================================
        QFriend qFriend = QFriend.friend;

        Boolean checkFriend = jpaQueryFactory.from(qFriend)
                .where(qFriend.me.eq(sendUser)
                        .and(qFriend.you.eq(receiveUser)))
                .fetchFirst() != null;

        if (checkFriend)
        {
            return new DefaultRsp("이미 친구 관계입니다.");
        }
        //==========================================

        //==========================================
        QTap qTap = QTap.tap;
        Tap checkrecievetap = jpaQueryFactory.select(qTap)
        .from(qTap)
                .where(qTap.sendUser.eq(receiveUser)
                        .and(qTap.receiveUser.eq(sendUser))
                        .and(qTap.status.eq(0)))
                .fetchOne();
        if (checkrecievetap != null)
        {
            checkrecievetap.setStatus(2);
            String roomId = UUID.randomUUID().toString();
            Friend newF = new Friend(receiveUser,sendUser,roomId);
            Friend newF2 = new Friend(sendUser,receiveUser,roomId);
            friendRepository.save(newF);
            friendRepository.save(newF2);
            tapRepository.save(checkrecievetap);
            return new DefaultRsp("상대방의 요청을 수락 하였습니다.");
        }
        //==========================================

        Boolean checksendtap = jpaQueryFactory.from(qTap)
                .where(qTap.sendUser.eq(sendUser)
                        .and(qTap.receiveUser.eq(receiveUser))
                        .and(qTap.status.eq(0)))
                .fetchFirst() != null;
        if (checksendtap)
        {
            return new DefaultRsp("이미 상대에게 요청을 보낸 상태입니다.");
        }
        //====================================================

        Tap newTap = new Tap(sendUser,receiveUser);
        tapRepository.save(newTap);
        //sendSMS(); // 여기서 상대방번호가 매개변수로 들어가야하지만 일단은 여기까지만
        return new DefaultRsp("정상적으로 처리 되었습니다.");
    }

    // 1인당 탭횟수 제한
    private void sendSMS() {
        String api_key = "a";
        String api_secret = "a";
        Message coolsms = new Message(api_key, api_secret);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("to", "01040343120");   // 탭요청 알람을 받기위해서 정확하게 기재해주세요
                                           // ㅎ
        params.put("from", "01066454534"); //사전에 사이트에서 번호를 인증하고 등록하여야 함 // 070 번호하나사고
        params.put("type", "SMS");
        params.put("text", "이승준 님이 탭 요청을 하였습니다..!"); //메시지 내용
        params.put("app_version", "test app 1.2");
        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString()); //전송 결과 출력
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }
    }
}
