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
    @GetMapping("/lsj/test") // dbSet 이라서 그냥 냅둠..
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
}
