package com.project.contap;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class realTest {
    //여기다테스트 해보고싶은코드 작성하면 편할것같아서 추가해봤어요
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    void test()
    {
        List<String> abc = new ArrayList<>();
        abc.add("1");
        abc.add("2");
        abc.add("3");
        List<String> abc2 = new ArrayList<>();
        abc2.add("4");
        abc2.add("5");
        abc2.add("6");
        abc.addAll(abc2);
        System.out.println(abc);
    }

    @Test
    void test2()
    {
        ListOperations<String, String> listOpsforRoomstatus = redisTemplate.opsForList();
        listOpsforRoomstatus.rightPush("test1","test"); // [0] = 보낸사람 , [1] = 채팅방 인원수
        listOpsforRoomstatus.rightPop("test1");
        listOpsforRoomstatus.rightPush("test1","test");
    }
}
