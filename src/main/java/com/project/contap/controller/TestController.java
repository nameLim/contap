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
}
