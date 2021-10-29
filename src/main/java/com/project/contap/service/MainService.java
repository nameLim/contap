package com.project.contap.service;

import com.project.contap.dto.QCardDto;
import com.project.contap.dto.SearchRequestDto;
import com.project.contap.dto.UserRequestDto;
import com.project.contap.dto.UserResponseDto;
import com.project.contap.model.HashTag;
import com.project.contap.model.QCard;
import com.project.contap.model.QUser;
import com.project.contap.model.User;
import com.project.contap.repository.HashTagRepositoty;
import com.project.contap.repository.UserRepository;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.util.GetRandom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class MainService {
    private final HashTagRepositoty hashTagRepositoty;
    private final UserRepository userRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Autowired
    public MainService(
            HashTagRepositoty hashTagRepositoty,
            JPAQueryFactory jpaQueryFactory,
            UserRepository userRepository)
    {
        this.hashTagRepositoty=hashTagRepositoty;
        this.jpaQueryFactory =jpaQueryFactory;
        this.userRepository = userRepository;
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
                                hu.hashTagsString
                        )).distinct()
                .from(hu)
                .where(hu.tags.any().id.in(ids2))
                .offset(9).limit(9)
                .fetch();
        return abc;
    }
    public List<UserRequestDto> searchuser(SearchRequestDto tagsandtype) {
        BooleanBuilder builder = new BooleanBuilder();
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
                                hu.hashTagsString
                        )).distinct()
                .from(hu)
                .where(builder)
                .offset(9).limit(9)
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
                                hu.hashTagsString
                        )
                )
                .from(hu)
                .where(hu.user.id.eq(userId))
                .fetch();
        return abc;
    }

    public List<UserResponseDto> getUserDtoList(UserDetailsImpl userDetails) {
        List<User> users = userRepository.findAll();
        return UserResponseDto.listOf(users, userDetails);
    }
}
