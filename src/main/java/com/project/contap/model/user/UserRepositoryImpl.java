package com.project.contap.model.user;

import com.project.contap.common.SearchRequestDto;
import com.project.contap.model.friend.QFriend;
import com.project.contap.model.tap.QTap;
import com.project.contap.model.user.dto.UserRequestDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Random;

public class UserRepositoryImpl implements CostomUserRepository{
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<UserRequestDto> findMysendORreceiveTapUserInfo(Long userId,int type)
    {
        QTap qtap = QTap.tap;
        if (type == 0)
            return queryFactory
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
                    .where(qtap.sendUser.id.eq(userId))
                    .fetch();
        else
            return queryFactory
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
                    .where(qtap.sendUser.id.eq(userId))
                    .fetch();

    }

    //사실 페이지기능을 고려해서 만들어진 쿼리였는데
    //지금은 페이지가 아니라 다 불러오는거로 바뀌엇으니까. 차후에 수정할필요가있다.
    //하지만 어찌될지몰라서 남겨둠.
    @Override
    public List<UserRequestDto> findMyFriendsById(Long userId,List<String> orderList)
    {
        QFriend qfriend = QFriend.friend;
        return queryFactory
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
                .where(qfriend.me.id.eq(userId)
                        .and(qfriend.roomId.in(orderList)))
                .fetch();
    }
    @Override
    public List<UserRequestDto> findAllByTag(SearchRequestDto tagsandtype)
    {
        BooleanBuilder builder = new BooleanBuilder();
        int page = 9*tagsandtype.getPage();
        QUser hu = QUser.user;
        if (tagsandtype.getType() == 0)
            for (String tagna : tagsandtype.getSearchTags()) {
                builder.or(hu.hashTagsString.contains("@"+tagna+"@"));
            }
        else
            for (String tagna : tagsandtype.getSearchTags()) {
                builder.and(hu.hashTagsString.contains("@"+tagna+"@"));
            }

        if(tagsandtype.getField() != 3)
        {
            builder.and(hu.field.eq(tagsandtype.getField()));
        }
        return queryFactory
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
    }
    @Override
    public List<UserRequestDto> getRandomUser(Long usercnt)
    {
        Long check1 = usercnt/9;
        Long check2 = usercnt%9;
        Long page = check1;
        if(check2.equals(0L))
            page = page - 1L;
        Random random = new Random();
        QUser hu = QUser.user;
        return queryFactory
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
                .offset(0).limit(9)
                .fetch();
        //귀찮.. 다음에 페이징처리하자
    }

    @Override
    public Boolean existUserByUserName(String userName)
    {

        QUser qUser = QUser.user;
        return  queryFactory.from(qUser)
                .where(qUser.userName.eq(userName))
                .fetchFirst() != null;
    }
    @Override
    public Boolean existUserByPhoneNumber(String phoneNumber)
    {
        QUser qUser = QUser.user;
        return  queryFactory.from(qUser)
                .where(qUser.phoneNumber.eq(phoneNumber))
                .fetchFirst() != null;
    }



}
