package com.project.contap.controller;

import com.project.contap.exception.ContapException;
import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.model.User;
import com.project.contap.repository.CardRepository;
import com.project.contap.repository.HashTagRepositoty;
import com.project.contap.repository.UserRepository;
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
    private Long test1RunTime = 0L;
    private Long test2RunTime = 0L;

    @Autowired
    public TestController(UserRepository userRepository,HashTagRepositoty hashTagRepositoty,CardRepository cardRepository) {
        this.userRepository = userRepository;
        this.hashTagRepositoty =hashTagRepositoty;
        this.cardRepository =  cardRepository;
    }

    @GetMapping("/test/lsj")
    public void registerUser() throws ContapException {
        User user = userRepository.findById(1L).orElse(null);
        User user2 = userRepository.lsjfind(1L);
        System.out.println(user);
        System.out.println(user2);
    }
    @GetMapping("/lsj/test")
    public void test() throws ContapException {

        HashTag ht1 = hashTagRepositoty.findById(301L).orElse(null);
        HashTag ht2= hashTagRepositoty.findById(302L).orElse(null);
        HashTag ht3= hashTagRepositoty.findById(303L).orElse(null);
        HashTag ht4= hashTagRepositoty.findById(304L).orElse(null);
        HashTag ht5= hashTagRepositoty.findById(305L).orElse(null);

        for(long i = 1 ; i< 5001 ;i++)// 1~300
        {
            User user = userRepository.findById(i).orElse(null);
            user.getTags().add(ht1);
            user.getTags().add(ht2);
            user.getTags().add(ht3);
            user.getTags().add(ht4);
            user.getTags().add(ht5);
            userRepository.save(user);
        }

        for(long i = 801 ; i< 1701 ;i++)// 1~300
        {
            Card user = cardRepository.findById(i).orElse(null);
            user.getTags().add(ht1);
            user.getTags().add(ht2);
            user.getTags().add(ht3);
            user.getTags().add(ht4);
            user.getTags().add(ht5);
            cardRepository.save(user);
        }
    }

    @GetMapping("/test1")
    Page<User> test1() throws ContapException {
        long startTime = System.currentTimeMillis();

        Random random = new Random();
        int page = random.nextInt(50);
        Page<User> ad = userRepository.findAll(PageRequest.of(page, 9));

        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        Logger log = LogManager.getLogger("APITime");
        log.error("test1user-----" + runTime+"ms");
        return ad;
    }
    @GetMapping("/test2")
    public List<Card> test2() throws ContapException {
        long startTime = System.currentTimeMillis();

        Random random = new Random();
        Long userId = Long.valueOf(random.nextInt(5000));
        User user = userRepository.findById(1L).orElse(null);
        List<Card> abc = cardRepository.findAllByUser(user);


        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        Logger log = LogManager.getLogger("APITime");
        log.error("test2card" + runTime+"ms");
        return abc;
    }

    @GetMapping("/test1/getRuntime")
    Long getTest1RunTime() throws ContapException {
        return test1RunTime;
    }

    @GetMapping("/test2/getRuntime")
    Long getTest2RunTime() throws ContapException {
        return test2RunTime;
    }
}
