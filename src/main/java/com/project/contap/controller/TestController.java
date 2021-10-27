package com.project.contap.controller;

import com.project.contap.dto.UserRequestDto;
import com.project.contap.exception.ContapException;
import com.project.contap.model.*;
import com.project.contap.repository.CardRepository;
import com.project.contap.repository.HashTagRepositoty;
import com.project.contap.repository.UserRepository;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class TestController {
    private final UserRepository userRepository;
    private final HashTagRepositoty hashTagRepositoty;
    private final CardRepository cardRepository;
    private final com.querydsl.jpa.impl.JPAQueryFactory jpaQueryFactory;
    private Long test1RunTime = 0L;
    private Long test2RunTime = 0L;
    private Long test1cnt = 0L;
    private Long test2cnt = 0L;
    private Boolean lsjcheck = true;
    @Autowired
    public TestController(UserRepository userRepository,HashTagRepositoty hashTagRepositoty,CardRepository cardRepository,com.querydsl.jpa.impl.JPAQueryFactory jpaQueryFactory) {
        this.userRepository = userRepository;
        this.hashTagRepositoty =hashTagRepositoty;
        this.cardRepository =  cardRepository;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @GetMapping("/test/lsj")
    public void registerUser() throws ContapException {
        User user = userRepository.findById(1L).orElse(null);
        User user2 = userRepository.lsjfind(1L);
        System.out.println(user);
        System.out.println(user2);
        Logger log = LogManager.getLogger("test3");
        log.error("sadasgfsams");
    }
    @GetMapping("/lsj/test")
    public String test() throws ContapException {
        for(long i = 1 ; i< 5001 ;i++)// 1~300 // 5001
        {
            User user = userRepository.findById(i).orElse(null);
            user.getTags().add(new HashTag(new Long(randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(randomRange(5001,5500))));
            userRepository.save(user);
        }

        for(long i = 5501 ; i< 7000 ;i++)// 1~300
        {
            Card user = cardRepository.findById(i).orElse(null);
            user.getTags().add(new HashTag(new Long(randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(randomRange(5001,5500))));
            cardRepository.save(user);
        }
        return "DBSet 완료염~";
    }
    public int randomRange(int n1, int n2) {
        return (int) (Math.random() * (n2 - n1 + 1)) + n1;
    }

    @GetMapping("/test1") // 페이지 기능이라서 빠른것뿐임.
    Page<User> test1() throws ContapException {
        long startTime = System.currentTimeMillis();
        Random random = new Random();
        int page = random.nextInt(100);
        Page<User> ad = userRepository.findAll(PageRequest.of(page, 9));
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        System.out.println("======================test1===============");
        System.out.println(runTime);
        System.out.println("======================test1===============");
        return ad;
    }
    @GetMapping("/test2") // 이것은 검색해서 유저의 모든것 조회한거 9개.
    public void test2() throws ContapException {
        long startTime = System.currentTimeMillis();
        QUser hu = QUser.user;
        List<Long> ids2 = Arrays.asList(new Long(randomRange(5001,5500)),new Long(randomRange(5001,5500)),new Long(randomRange(5001,5500)));
        QueryResults<User> abc;
        abc = jpaQueryFactory
                .select(hu).distinct()
                .from(hu)
                .where(hu.tags.any().id.in(ids2))
                .offset(9).limit(9)
                .fetchResults();

        List<User> ret = abc.getResults();
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        System.out.println("======================test2===============");
        System.out.println(runTime);
        System.out.println("======================test2===============");
    }

    @GetMapping("/test3") // 이것은 한 유저의 카드 10장 조회
    List<Card> getTest1123RunTime() throws ContapException {
        long startTime = System.currentTimeMillis();
        QCard hu = QCard.card;
        Random random = new Random();
        Long userId = Long.valueOf(random.nextInt(5000));
        QueryResults<Card> abc =  jpaQueryFactory
                .select(hu)
                .from(hu)
                .where(hu.user.id.eq(userId))
                .fetchResults();
        List<Card> ret = abc.getResults();
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        System.out.println("======================test3===============");
        System.out.println(runTime);
        System.out.println("======================test3===============");
        return ret;
    }

    @GetMapping("/test4") // 이것은 검색해서 유저의 정보만 카드,태그 제외
    public List<UserRequestDto> test4() throws ContapException {
        long startTime = System.currentTimeMillis();
        QUser hu = QUser.user;
        List<Long> ids2 = Arrays.asList(new Long(randomRange(5001,5500)),new Long(randomRange(5001,5500)),new Long(randomRange(5001,5500)));
        List<UserRequestDto> abc;
        abc = jpaQueryFactory
                .select(
                        Projections.constructor(UserRequestDto.class,
                                hu.id,
                                hu.email,
                                hu.profile,
                                hu.kakaoId,
                                hu.userName,
                                hu.pw
                        )).distinct()
                .from(hu)
                .where(hu.tags.any().id.in(ids2))
                .offset(9).limit(9)
                .fetch();

        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        System.out.println("======================test4===============");
        System.out.println(runTime);
        System.out.println("======================test4===============");
        return abc;
    }

    @GetMapping("/test5") // 이건 그냥 메인에서하는거.
    List<UserRequestDto> test5() throws ContapException {
        long startTime = System.currentTimeMillis();
        QUser hu = QUser.user;
        List<UserRequestDto> abc;
        Random random = new Random();
        int page = random.nextInt(100);
        abc = jpaQueryFactory
                .select(
                        Projections.constructor(UserRequestDto.class,
                                hu.id,
                                hu.email,
                                hu.profile,
                                hu.kakaoId,
                                hu.userName,
                                hu.pw
                        ))
                .from(hu)
                .offset(page).limit(9)
                .fetch();

        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        System.out.println("======================test1===============");
        System.out.println(runTime);
        System.out.println("======================test1===============");
        return abc;
    }

    @GetMapping("/test6") // 이것은 검색해서 유저의 정보만 카드 제외 태그는 검색됨 // 내일 테스트 해보자
    public List<User> test6() throws ContapException {
        long startTime = System.currentTimeMillis();
        QUser hu = QUser.user;
//        List<HashTag> ids2 = Arrays.asList(new HashTag(new Long(randomRange(5001,5500))),new HashTag(new Long(randomRange(5001,5500))),new HashTag(new Long(randomRange(5001,5500))));
        List<Long> ids2 = Arrays.asList(new Long(randomRange(5001,5500)),new Long(randomRange(5001,5500)),new Long(randomRange(5001,5500)));
        List<Tuple> abc;
        abc = jpaQueryFactory
                .select(
                                hu.id,
                                hu.email,
                                hu.profile,
                                hu.kakaoId,
                                hu.userName,
                                hu.pw,
                                hu.tags
                        ).distinct()
                .from(hu)
                .where(hu.tags.any().id.in(ids2))
                .offset(9).limit(9)
                .fetch();

        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        System.out.println("======================test4===============");
        System.out.println(runTime);
        System.out.println("======================test4===============");
        return null;
    }

    @GetMapping("/test1/getRuntime")
    List<Long> getTest1RunTime() throws ContapException {
        List<Long> a = new ArrayList<>();
        a.add(test1RunTime);
        a.add(test1cnt);
        return a;
    }

    @GetMapping("/test2/getRuntime")
    List<Long> getTest2RunTime() throws ContapException {
        List<Long> a = new ArrayList<>();
        a.add(test2RunTime);
        a.add(test2cnt);
        return a;
    }
}
