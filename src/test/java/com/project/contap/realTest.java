package com.project.contap;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class realTest {
    //여기다테스트 해보고싶은코드 작성하면 편할것같아서 추가해봤어요
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
}
