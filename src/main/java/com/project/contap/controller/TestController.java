package com.project.contap.controller;

import com.project.contap.dto.UserRequestDto;
import com.project.contap.exception.ContapException;
import com.project.contap.model.*;
import com.project.contap.repository.CardRepository;
import com.project.contap.repository.HashTagRepositoty;
import com.project.contap.repository.UserRepository;
import com.project.contap.util.GetRandom;
import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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
    @GetMapping("/lsj/test") // dbSet 이라서 그냥 냅둠..
    public String test() throws ContapException {
        for(long i = 1 ; i< 5001 ;i++)// 1~300 // 5001
        {
            User user = userRepository.findById(i).orElse(null);
            user.getTags().add(new HashTag(new Long(GetRandom.randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(GetRandom.randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(GetRandom.randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(GetRandom.randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(GetRandom.randomRange(5001,5500))));
            userRepository.save(user);
        }

        for(long i = 5501 ; i< 50000 ;i++)// 1~300
        {
            Card user = cardRepository.findById(i).orElse(null);
            user.getTags().add(new HashTag(new Long(GetRandom.randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(GetRandom.randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(GetRandom.randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(GetRandom.randomRange(5001,5500))));
            user.getTags().add(new HashTag(new Long(GetRandom.randomRange(5001,5500))));
            cardRepository.save(user);
        }
        return "DBSet 완료염~";
    }

    @GetMapping("/test2")
    public List<Card> test2() throws ContapException {
        long startTime = System.currentTimeMillis();
        test2cnt = test2cnt +1;
        Random random = new Random();
        Long userId = Long.valueOf(random.nextInt(5000));
        if(userId == 0L)
            userId = 1L;
        User user = userRepository.findById(userId).orElse(null);
        List<Card> abc = cardRepository.findAllByUser(userId);
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        test2RunTime = runTime + test2RunTime;
        Logger log = LogManager.getLogger("test2");
        log.error(runTime+"ms");
        return abc;
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

    @GetMapping("/test3")
    void getTest1123RunTime() throws ContapException {
        Logger log = LogManager.getLogger("test2");
        log.error("for test!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
    }
}
