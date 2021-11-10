package com.project.contap.model.user;

import com.project.contap.model.friend.QFriend;
import com.project.contap.model.tap.QTap;
import com.project.contap.model.user.dto.UserRequestDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;
import java.util.List;

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

}
