package com.project.contap.controller;

import com.project.contap.exception.ContapException;
import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.model.User;
import com.project.contap.repository.CardRepository;
import com.project.contap.repository.HashTagRepositoty;
import com.project.contap.repository.UserRepository;
import com.project.contap.service.ContapService;
import com.project.contap.service.MainService;
import com.project.contap.util.GetRandom;
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
    private final ContapService contapService;
    private final MainService mainService;
    private final com.querydsl.jpa.impl.JPAQueryFactory jpaQueryFactory;
    private Long test1RunTime = 0L;
    private Long test2RunTime = 0L;
    private Long test1cnt = 0L;
    private Long test2cnt = 0L;
    private Boolean lsjcheck = true;
    @Autowired
    public TestController(
            UserRepository userRepository,
            HashTagRepositoty hashTagRepositoty,
            CardRepository cardRepository,
            com.querydsl.jpa.impl.JPAQueryFactory jpaQueryFactory,
            ContapService contapService,
            MainService mainService
    ) {
        this.userRepository = userRepository;
        this.hashTagRepositoty =hashTagRepositoty;
        this.cardRepository =  cardRepository;
        this.jpaQueryFactory = jpaQueryFactory;
        this.contapService = contapService;
        this.mainService = mainService;
    }
    @GetMapping("/lsj/test") // dbSet 이라서 그냥 냅둠..
    public String test() throws ContapException {
        for(long i = 1 ; i< 305 ;i++)// 1~5007번까지 있오
        {
            User user = userRepository.findById(i).orElse(null);
            HashTag has1 = hashTagRepositoty.findById(new Long(GetRandom.randomRange(1,10))).orElse(null);
            HashTag has2 = hashTagRepositoty.findById(new Long(GetRandom.randomRange(1,10))).orElse(null);
            HashTag has3 = hashTagRepositoty.findById(new Long(GetRandom.randomRange(1,10))).orElse(null);
            HashTag has4 = hashTagRepositoty.findById(new Long(GetRandom.randomRange(1,10))).orElse(null);
            HashTag has5 = hashTagRepositoty.findById(new Long(GetRandom.randomRange(1,10))).orElse(null);
            List<HashTag> ENSENS = new ArrayList<>();
            ENSENS.add(has1);
            ENSENS.add(has2);
            ENSENS.add(has3);
            ENSENS.add(has4);
            ENSENS.add(has5);
            user.getTags().add(has1);
            user.getTags().add(has2);
            user.getTags().add(has3);
            user.getTags().add(has4);
            user.getTags().add(has5);
            String dud = "";
            String dlf = "";
            for(HashTag ha :ENSENS)
            {

                if(ha.getType() == 0)
                {
                    dud = dud +"@"+ha.getName();
                }
                else
                {
                    dlf = dlf +"@"+ha.getName();
                }
            }
            user.setHashTagsString(dud+"@_"+dlf+"@");
            userRepository.save(user);
        }

        for(long i = 1 ; i< 2000 ;i++)// 1~300
        {
            Card user = cardRepository.findById(i).orElse(null);
            HashTag has1 = hashTagRepositoty.findById(new Long(GetRandom.randomRange(1,10))).orElse(null);
            HashTag has2 = hashTagRepositoty.findById(new Long(GetRandom.randomRange(1,10))).orElse(null);
            HashTag has3 = hashTagRepositoty.findById(new Long(GetRandom.randomRange(1,10))).orElse(null);
            HashTag has4 = hashTagRepositoty.findById(new Long(GetRandom.randomRange(1,10))).orElse(null);
            HashTag has5 = hashTagRepositoty.findById(new Long(GetRandom.randomRange(1,10))).orElse(null);
            List<HashTag> ENSENS = new ArrayList<>();
            ENSENS.add(has1);
            ENSENS.add(has2);
            ENSENS.add(has3);
            ENSENS.add(has4);
            ENSENS.add(has5);
            user.getTags().add(has1);
            user.getTags().add(has2);
            user.getTags().add(has3);
            user.getTags().add(has4);
            user.getTags().add(has5);
            String dud = "";
            String dlf = "";
            for(HashTag ha :ENSENS)
            {

                if(ha.getType() == 0)
                {
                    dud = dud +"@"+ha.getName();
                }
                else
                {
                    dlf = dlf +"@"+ha.getName();
                }
            }
            user.setTagsString(dud+"@_"+dlf+"@");
            cardRepository.save(user);
        }
        return "DBSet 완료염~";
    }
    @GetMapping("/lsj/test2") // dbSet 이라서 그냥 냅둠..
    public String test22() throws ContapException {
        User tmdwns = new User(5001L);
        mainService.dotap(tmdwns,5002L);
        mainService.dotap(tmdwns,5003L);
        mainService.dotap(tmdwns,5004L);
        mainService.dotap(tmdwns,5005L);
        mainService.dotap(tmdwns,5006L);
        mainService.dotap(tmdwns,5007L);
        return "친구신청완료~";
    }
    @GetMapping("/lsj/test3") // dbSet 이라서 그냥 냅둠..
    public String test33() throws ContapException {
//        contapService.tapReject(2L);
//        contapService.tapReject(5L);
//        contapService.rapAccept(1L);
//        contapService.rapAccept(4L);
        return "친구거절및수락완료~";
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
//        List<Card> abc = cardRepository.findAllByUser(userId);
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;
        test2RunTime = runTime + test2RunTime;
        Logger log = LogManager.getLogger("test2");
        log.error(runTime+"ms");
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

    @GetMapping("/test3")
    void getTest1123RunTime() throws ContapException {
        int num = GetRandom.randomRange(5000,80000);
        List<String> ls = new ArrayList<String>();
        HashMap<String, Boolean> map = new HashMap<String,Boolean>();
        String abc = "";
        for (int i = 0; i <= 90000; i++)
        {
            String a = UUID.randomUUID().toString();
            if (i == num)
                abc = a;
            ls.add(a);
            map.put(a,true);
        }
        long startTime = System.currentTimeMillis();
        System.out.println(ls.contains(abc));
        long endTime = System.currentTimeMillis();
        long runTime = endTime - startTime;

        long startTime2 = System.currentTimeMillis();
        System.out.println(map.get(abc));
        long endTime2 = System.currentTimeMillis();
        long runTime2 = endTime - startTime;

        System.out.println("1==" + runTime);
        System.out.println("2==" + runTime2);


    }



    @GetMapping("/forclient1/{id}") // 한유저가쓴 카드 모두조회
    List<Card> forclient1(
            @PathVariable Long id
    ) throws ContapException {
        User user = userRepository.findById(id).orElse(null);
        if (user == null)
            return null;
        return cardRepository.findAllByUser(user);
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
