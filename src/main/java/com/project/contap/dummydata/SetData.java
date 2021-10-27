package com.project.contap.dummydata;

import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.model.HashUser;
import com.project.contap.model.User;
import com.project.contap.repository.CardRepository;
import com.project.contap.repository.HashTagRepositoty;
import com.project.contap.repository.HashUserRepository;
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
    HashUserRepository hashUserRepository;
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
        HashTag ht1 = hashTagRepositoty.findById(301L).orElse(null);
        HashTag ht2= hashTagRepositoty.findById(302L).orElse(null);
        HashTag ht3= hashTagRepositoty.findById(303L).orElse(null);
        HashTag ht4= hashTagRepositoty.findById(304L).orElse(null);
        HashTag ht5= hashTagRepositoty.findById(305L).orElse(null);
        for(long i = 1 ; i< 301 ;i++)// 1~300
        {
            User user = userRepository.findById(i).orElse(null);
            HashUser hu1 = new HashUser(user,ht1);
            HashUser hu2 = new HashUser(user,ht2);
            HashUser hu3 = new HashUser(user,ht3);
            HashUser hu4 = new HashUser(user,ht4);
            HashUser hu5 = new HashUser(user,ht5);
            hashUserRepository.save(hu1);
            hashUserRepository.save(hu2);
            hashUserRepository.save(hu3);
            hashUserRepository.save(hu4);
            hashUserRepository.save(hu5);

        }

        for(long i = 1 ; i< 301 ;i++)// 1~300
        {
            User user = userRepository.findById(i).orElse(null);
            Card ca1 = new Card(user,1,String.format("title%d", i),String.format("content%d", i),String.format("filePath%d", i));
            Card ca2 = new Card(user,2,String.format("title%d", i),String.format("content%d", i),String.format("filePath%d", i));
            Card ca3 = new Card(user,3,String.format("title%d", i),String.format("content%d", i),String.format("filePath%d", i));
            cardRepository.save(ca1);
            cardRepository.save(ca2);
            cardRepository.save(ca3);
//            if(user != null) {
//                user.getCards().add(ca1);
//                user.getCards().add(ca2);
//                user.getCards().add(ca3);
//                userRepository.save(user);
//            }
        }
    }

}

