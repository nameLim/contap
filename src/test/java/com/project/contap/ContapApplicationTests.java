package com.project.contap;


import com.project.contap.dto.FrontResponseCardDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;

import java.util.*;


@SpringBootTest
class ContapApplicationTests {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private ZSetOperations<String, String> zSetOperations;
    @Test
    void contextLoads() {
        zSetOperations = redisTemplate.opsForZSet();
        zSetOperations.add("user1","abcabc1",1);
        zSetOperations.add("user1","abcabc2",2);
        zSetOperations.add("user1","abcabc3",3);
        zSetOperations.add("user1","abcabc4",4);
        zSetOperations.add("user1","abcabc5",5);
        zSetOperations.add("user1","abcabc6",6);
        zSetOperations.add("user1","abcabc7",7);
        zSetOperations.add("user1","abcabc8",8);
        zSetOperations.add("user1","abcabc9",9);
        zSetOperations.add("user1","abcabc10",10);
        zSetOperations.add("user1","abcabc11",11);
        zSetOperations.add("user1","abcabc12",12);
        zSetOperations.add("user1","abcabc13",13);
        double checklsj = zSetOperations.score("user1","abcabc1");
        double destdou = 18;
        zSetOperations.incrementScore("user1","abcabc1",destdou-checklsj);
        java.util.Set<ZSetOperations.TypedTuple<String>> abc = zSetOperations.reverseRangeWithScores("user1",0,8);
        java.util.Set<ZSetOperations.TypedTuple<String>> abc2 = zSetOperations.reverseRangeWithScores("user1",9,17);
        List<String> values = new ArrayList<>();
        for (Iterator<ZSetOperations.TypedTuple<String>> iterator = abc.iterator(); iterator.hasNext();) {
            ZSetOperations.TypedTuple<String> typedTuple = iterator.next();
            values.add(typedTuple.getValue()+"@"+typedTuple.getScore().toString());
        }
        System.out.println(abc);
    }

    @Test
    void contextLoads2() {
        HashOperations<String, String, String> hashOpsAlarmInfo = redisTemplate.opsForHash();
        hashOpsAlarmInfo.put("ALARM_INFO","userEmail1","true");
        hashOpsAlarmInfo.put("ALARM_INFO","userEmail2","true");
        hashOpsAlarmInfo.put("ALARM_INFO","userEmail3","true");
        hashOpsAlarmInfo.put("ALARM_INFO","userEmail4","true");
        String abc = hashOpsAlarmInfo.get("ALARM_INFO","userEmail4"); // "true"
        String abc2 = hashOpsAlarmInfo.get("ALARM_INFO","userEmail5");// null
        System.out.println(abc);
        System.out.println(abc2);
    }

    @Test
    void contextLoads3() {
//        final ListOperations<String, String> stringStringListOperations = redisTemplate.opsForList();
//        stringStringListOperations.rightPop("LSJ");
//        stringStringListOperations.rightPush("LSJ","sad/0");
//        Long splitIndex = stringStringListOperations.indexOf("LSJ","/");
//        List<String> lastSender = stringStringListOperations.range("LSJ",0L,splitIndex);
//
//        System.out.println(stringStringListOperations);
    }

}
