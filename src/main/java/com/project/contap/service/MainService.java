package com.project.contap.service;

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
    private final JPAQueryFactory jpaQueryFactory;
    private final TapRepository tapRepository;
    private final FriendRepository friendRepository;
    private final UserService userService;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate redisTemplate;
    private final ChannelTopic channelTopic;

    @Autowired
    public MainService(
            HashTagRepositoty hashTagRepositoty,
            JPAQueryFactory jpaQueryFactory,
            UserRepository userRepository,
            TapRepository tapRepository,
            FriendRepository friendRepository,
            UserService userService,
            ChatRoomRepository chatRoomRepository,
            RedisTemplate redisTemplate,
            ChannelTopic channelTopic)
    {
        this.hashTagRepositoty=hashTagRepositoty;
        this.jpaQueryFactory =jpaQueryFactory;
        this.userRepository = userRepository;
        this.tapRepository = tapRepository;
        this.friendRepository = friendRepository;
        this.userService = userService;
        this.chatRoomRepository = chatRoomRepository;
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }

    public List<HashTag> getHashTag() {
        return hashTagRepositoty.findAll();
    }

    public List<UserRequestDto> fortestsearchuser() {
        QUser hu = QUser.user;
        List<Long> ids2 = Arrays.asList(new Long(RandomNumberGeneration.randomRange(1,10)),new Long(RandomNumberGeneration.randomRange(1,10)),new Long(RandomNumberGeneration.randomRange(1,10)));
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
        User receiveUser = userRepository.findById(otherUserId).orElse(null);
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
        String receiverssesion = chatRoomRepository.getSessionId(receiveUser.getEmail());
        if(receiverssesion != null) {
            ChatMessageDTO msg = new ChatMessageDTO();
            msg.setType(3);
            msg.setReciever(receiveUser.getEmail());
            msg.setWriter(sendUser.getEmail());
            msg.setMessage("Tap요청이 들어왔어요!");
            msg.setSessionId(receiverssesion);
            redisTemplate.convertAndSend(channelTopic.getTopic(), msg);
        }
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