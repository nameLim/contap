package com.project.contap.dummydata;

import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.model.User;
import com.project.contap.repository.CardRepository;
import com.project.contap.repository.HashTagRepositoty;
import com.project.contap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;

@Component
public class SetData implements ApplicationRunner{

    @Autowired
    UserRepository userRepository;
    @Autowired
    HashTagRepositoty hashTagRepositoty;
    @Autowired
    CardRepository cardRepository;


    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        for(int i = 0 ; i< 5000 ;i++)// 1~300
        {
            User user = new User(
                    String.format("userid%d", i)
                    ,String.format("userpw%d", i)
                    ,String.format("username%d", i));
            userRepository.save(user);
        }

        HashTag ht1 = new HashTag("축구",1);
        HashTag ht2 = new HashTag("농구",1);
        HashTag ht3 = new HashTag("야구",1);
        HashTag ht4 = new HashTag("배구",1);
        HashTag ht5 = new HashTag("헬스",1);
        HashTag ht6 = new HashTag("Spring",0);
        HashTag ht7 = new HashTag("React",0);
        HashTag ht8 = new HashTag("Java",0);
        HashTag ht9 = new HashTag("Html",0);
        HashTag ht0 = new HashTag("C",0);
        HashTag ht11 = new HashTag("피자",1);
        HashTag ht12 = new HashTag("햄버거",1);
        HashTag ht13 = new HashTag("삼겹살",1);
        HashTag ht14 = new HashTag("탕수육",1);
        HashTag ht15 = new HashTag("짜장면",1);
        HashTag ht16 = new HashTag("웬도우",0);
        HashTag ht17 = new HashTag("리눅스",0);
        HashTag ht18 = new HashTag("데이터베이스",0);
        HashTag ht19 = new HashTag("노드",0);
        HashTag ht20 = new HashTag("웹소켓",0);
        hashTagRepositoty.save(ht1);
        hashTagRepositoty.save(ht2);
        hashTagRepositoty.save(ht3);
        hashTagRepositoty.save(ht4);
        hashTagRepositoty.save(ht5);
        hashTagRepositoty.save(ht6);
        hashTagRepositoty.save(ht7);
        hashTagRepositoty.save(ht8);
        hashTagRepositoty.save(ht9);
        hashTagRepositoty.save(ht0);
        hashTagRepositoty.save(ht11);
        hashTagRepositoty.save(ht12);
        hashTagRepositoty.save(ht13);
        hashTagRepositoty.save(ht14);
        hashTagRepositoty.save(ht15);
        hashTagRepositoty.save(ht16);
        hashTagRepositoty.save(ht17);
        hashTagRepositoty.save(ht18);
        hashTagRepositoty.save(ht19);
        hashTagRepositoty.save(ht20);



        for(long i = 1 ; i< 5001 ;i++)// 801~
        {
            User user = userRepository.findById(i).orElse(null);
            Card ca1 = new Card(user,1L,String.format("title%d", i),String.format("content%d", i));
            Card ca2 = new Card(user,2L,String.format("title%d", i),String.format("content%d", i));
            Card ca3 = new Card(user,3L,String.format("title%d", i),String.format("content%d", i));
            Card ca4 = new Card(user,1L,String.format("title%d", i),String.format("content%d", i));
            Card ca5 = new Card(user,2L,String.format("title%d", i),String.format("content%d", i));
            Card ca6 = new Card(user,3L,String.format("title%d", i),String.format("content%d", i));
            Card ca7 = new Card(user,1L,String.format("title%d", i),String.format("content%d", i));
            Card ca8 = new Card(user,2L,String.format("title%d", i),String.format("content%d", i));
            Card ca9 = new Card(user,3L,String.format("title%d", i),String.format("content%d", i));
            Card ca0 = new Card(user,3L,String.format("title%d", i),String.format("content%d", i));
            cardRepository.save(ca1);
            cardRepository.save(ca2);
            cardRepository.save(ca3);
            cardRepository.save(ca4);
            cardRepository.save(ca5);
            cardRepository.save(ca6);
            cardRepository.save(ca7);
            cardRepository.save(ca8);
            cardRepository.save(ca9);
            cardRepository.save(ca0);

        }
    }

}

