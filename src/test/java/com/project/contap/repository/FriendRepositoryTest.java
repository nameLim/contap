package com.project.contap.repository;

import com.project.contap.common.Common;
import com.project.contap.model.card.Card;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.card.dto.QCardDto;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class FriendRepositoryTest {

    @Autowired
    private FriendRepository friendRepository;

    @Autowired
    private UserRepository userRepository;


    Long id1;
    Long id2;
    Long id3;
    Long id4;

    @BeforeEach
    void setUp() throws Exception {
        User user1 = User.builder()
                .email("user1@gmail.com")
                .pw("123")
                .userName("user1")
                .field(0).build();
        User user2 = User.builder()
                .email("user2@gmail.com")
                .pw("123")
                .userName("user2")
                .field(1).build();
        User user3 = User.builder()
                .email("user3@gmail.com")
                .pw("123")
                .userName("user3")
                .field(1).build();
        User user4 = User.builder()
                .email("user4@gmail.com")
                .pw("123")
                .userName("user4")
                .field(1).build();

        id1 = userRepository.save(user1).getId();
        id2 = userRepository.save(user2).getId();
        id3 = userRepository.save(user3).getId();
        id4 = userRepository.save(user4).getId();

        Friend fir = Friend.builder()
                .roomId("roomId")
                .me(user1)
                .you(user2)
                .build();
        Friend sec = Friend.builder()
                .roomId("roomId")
                .me(user2)
                .you(user1)
                .build();
        friendRepository.save(fir);
        friendRepository.save(sec);

        Friend fir1 = Friend.builder()
                .roomId("roomId")
                .me(user1)
                .you(user3)
                .build();
        Friend sec1 = Friend.builder()
                .roomId("roomId")
                .me(user3)
                .you(user1)
                .build();
        friendRepository.save(fir1);
        friendRepository.save(sec1);

        Friend fir2 = Friend.builder()
                .roomId("roomId")
                .me(user1)
                .you(user4)
                .build();
        Friend sec2 = Friend.builder()
                .roomId("roomId")
                .me(user4)
                .you(user1)
                .build();
        friendRepository.save(fir2);
        friendRepository.save(sec2);

        Friend fir3 = Friend.builder()
                .roomId("roomId")
                .me(user3)
                .you(user4)
                .build();
        Friend sec3 = Friend.builder()
                .roomId("roomId")
                .me(user4)
                .you(user3)
                .build();
        friendRepository.save(fir3);
        friendRepository.save(sec3);
    }

    @Test
    @DisplayName("친구관계 체크")
    void findByCategory01() {
        User user1 = userRepository.findById(id1).orElse(null);
        User user2 = userRepository.findById(id2).orElse(null);
        User user3 = userRepository.findById(id3).orElse(null);
        User user4 = userRepository.findById(id4).orElse(null);

        assertTrue(friendRepository.checkFriend(user1,user2));
        assertTrue(friendRepository.checkFriend(user1,user3));
        assertTrue(friendRepository.checkFriend(user1,user4));
        assertTrue(friendRepository.checkFriend(user3,user4));
        assertTrue(friendRepository.checkFriend(user4,user3));
        assertTrue(friendRepository.checkFriend(user4,user1));
        assertTrue(friendRepository.checkFriend(user3,user1));
        assertTrue(friendRepository.checkFriend(user2,user1));
        assertFalse(friendRepository.checkFriend(user2,user3));
        assertFalse(friendRepository.checkFriend(user2,user4));
        assertFalse(friendRepository.checkFriend(user3,user2));
        assertFalse(friendRepository.checkFriend(user4,user2));
    }
}