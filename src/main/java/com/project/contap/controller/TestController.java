package com.project.contap.controller;

import com.project.contap.exception.ContapException;
import com.project.contap.model.card.Card;
import com.project.contap.model.hashtag.HashTag;
import com.project.contap.model.user.User;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.user.UserRepository;
import com.project.contap.service.ContapService;
import com.project.contap.service.MainService;
import com.project.contap.common.util.RandomNumberGeneration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class TestController {
    private final UserRepository userRepository;
    private final HashTagRepositoty hashTagRepositoty;
    private final CardRepository cardRepository;
    @Autowired
    public TestController(
            UserRepository userRepository,
            HashTagRepositoty hashTagRepositoty,
            CardRepository cardRepository
    ) {
        this.userRepository = userRepository;
        this.hashTagRepositoty =hashTagRepositoty;
        this.cardRepository =  cardRepository;
    }

    @GetMapping("/forclient1/{id}") // 한유저가쓴 카드 모두조회
    List<Card> forclient1(
            @PathVariable Long id
    ) throws ContapException {
        return null;
    }

    @GetMapping("/forclient2/{id}") // 한유저가쓴 카드 모두조회
    User forclient2(
            @PathVariable Long id
    ) throws ContapException {
        User user = userRepository.findById(id).orElse(null);
        return user;
    }
    @GetMapping("/forclient3/{id}") // 한유저가쓴 카드 모두조회
    User forclient3(
            @PathVariable String id
    ) throws ContapException {
        User user = userRepository.findByEmail(id).orElse(null);
        return user;
    }
    @GetMapping("/forclient4/{id}") // 한유저가쓴 카드 모두조회
    Card forclient4(
            @PathVariable Long id
    ) throws ContapException {
        Card card = cardRepository.findById(id).orElse(null);
        return card;
    }

    @GetMapping("/HashSet") // 한유저가쓴 카드 모두조회
    void forclient4() throws ContapException {
        List<HashTag> hashs = new ArrayList<>();
        hashs.add(new HashTag("Flutter",0));
        hashs.add(new HashTag("제플린",0));
        hashs.add(new HashTag("프로크리에이트",0));
        hashs.add(new HashTag("파워포인트",0));
        hashs.add(new HashTag("React Native",0));
        hashs.add(new HashTag("React",0));
        hashs.add(new HashTag("Node.js",0));
        hashs.add(new HashTag("Vue.js",0));
        hashs.add(new HashTag("Python",0));
        hashs.add(new HashTag("C++",0));
        hashs.add(new HashTag("Angular",0));
        hashs.add(new HashTag("애프터이펙트",0));
        hashs.add(new HashTag("Go",0));
        hashs.add(new HashTag("C#",0));
        hashs.add(new HashTag("TypeScript",0));
        hashs.add(new HashTag("SQL",0));
        hashs.add(new HashTag("MySQL",0));
        hashs.add(new HashTag("JSP",0));
        hashs.add(new HashTag("Django",0));
        hashs.add(new HashTag("FastAPI",0));
        hashs.add(new HashTag("PostgreSQL",0));
        hashs.add(new HashTag("프리미어",0));
        hashs.add(new HashTag("NestJS",0));
        hashs.add(new HashTag("PMO",0));
        hashs.add(new HashTag("EEO",0));
        hashs.add(new HashTag("FCC",0));
        hashs.add(new HashTag("QFD",0));
        hashs.add(new HashTag("VR",0));
        hashs.add(new HashTag("Zemax",0));
        hashs.add(new HashTag("WAN",0));
        hashs.add(new HashTag("Java",0));
        hashs.add(new HashTag("JavaScript",0));
        hashs.add(new HashTag("피그마",0));
        hashs.add(new HashTag("스케치",0));
        hashs.add(new HashTag("오토캐드",0));
        hashs.add(new HashTag("스케치업",0));
        hashs.add(new HashTag("포토샵",0));
        hashs.add(new HashTag("일러스트레이터",0));
        hashs.add(new HashTag("어도비XD",0));
        hashs.add(new HashTag("인디자인",0));
        hashs.add(new HashTag("밥먹기",1));
        hashs.add(new HashTag("가만히 있기",1));
        hashs.add(new HashTag("뛰기",1));
        hashs.add(new HashTag("지오캐싱",1));
        hashs.add(new HashTag("걷기",1));
        hashs.add(new HashTag("숨쉬기",1));
        hashs.add(new HashTag("종이접기",1));
        hashs.add(new HashTag("피겨 스케이팅",1));
        hashTagRepositoty.saveAll(hashs);
    }
}
