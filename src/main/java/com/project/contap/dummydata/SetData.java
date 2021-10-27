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
import java.util.ArrayList;
import java.util.List;

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
        for(int i = 0 ; i< 300 ;i++)// 1~300
        {
            User user = new User(
                    String.format("userid%d", i)
                    ,String.format("userpw%d", i)
                    ,String.format("username%d", i));
            userRepository.save(user);
        }

        for(int i = 0 ; i< 500 ;i++) // 301~800
        {
            HashTag ht = new HashTag(
                    String.format("%dabc", i)
                    ,i%2);
            hashTagRepositoty.save(ht);
        }

        User user1 = userRepository.findById(1L).orElse(null);
        User user2 = userRepository.findById(1L).orElse(null);
        User user3 = userRepository.findById(1L).orElse(null);

        for(long i = 1 ; i< 301 ;i++)// 801~
        {
            User user = userRepository.findById(i).orElse(null);
            Card ca1 = new Card(user,1,String.format("title%d", i),String.format("content%d", i),String.format("filePath%d", i));
            Card ca2 = new Card(user,2,String.format("title%d", i),String.format("content%d", i),String.format("filePath%d", i));
            Card ca3 = new Card(user,3,String.format("title%d", i),String.format("content%d", i),String.format("filePath%d", i));
            cardRepository.save(ca1);
            cardRepository.save(ca2);
            cardRepository.save(ca3);
        }
    }

}

