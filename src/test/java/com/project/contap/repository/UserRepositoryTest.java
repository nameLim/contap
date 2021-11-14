package com.project.contap.repository;

import com.project.contap.common.SearchRequestDto;
import com.project.contap.model.friend.Friend;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.UserMainDto;
import com.project.contap.model.user.dto.UserTapDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private TapRepository tapRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FriendRepository friendRepository;


    List<Long> id = new ArrayList<>();


    @BeforeEach
    void setUp() throws Exception {
        User user1 = User.builder()
                .email("user1@gmail.com")
                .pw("123")
                .userName("user1")
                .profile("profile1")
                .hashTagsString("@JAVA@_@축구@")
                .phoneNumber("01011112222")
                .field(0).build();
        User user2 = User.builder()
                .email("user2@gmail.com")
                .pw("123")
                .userName("user2")
                .profile("profile2")
                .hashTagsString("@C@_@야구@")
                .phoneNumber("01011112223")
                .field(1).build();
        User user3 = User.builder()
                .email("user3@gmail.com")
                .pw("123")
                .userName("user3")
                .profile("profile3")
                .hashTagsString("@C#@_@배구@")
                .phoneNumber("01011112224")
                .field(2).build();
        User user4 = User.builder()
                .email("user4@gmail.com")
                .pw("123")
                .userName("user4")
                .profile("profile4")
                .hashTagsString("@C++@_@축구 시청@")
                .phoneNumber("01011112225")
                .field(1).build();
        User user5 = User.builder()
                .email("user5@gmail.com")
                .pw("123")
                .userName("user5")
                .profile("profile5")
                .hashTagsString("@C@_@축구 게임하기@")
                .phoneNumber("01011112226")
                .field(1).build();
        User user6 = User.builder()
                .email("user6@gmail.com")
                .pw("123")
                .userName("user6")
                .profile("profile6")
                .hashTagsString("@spring@_@축구@")
                .phoneNumber("01011112227")
                .field(1).build();
        User user7 = User.builder()
                .email("user7@gmail.com")
                .pw("123")
                .userName("user7")
                .profile("profile7")
                .hashTagsString("@JAVA@_@축구@")
                .phoneNumber("01011112228")
                .field(1).build();
        User user8 = User.builder()
                .email("user8@gmail.com")
                .pw("123")
                .userName("user8")
                .profile("profile8")
                .hashTagsString("@JAVA@_@축구@야구@")
                .phoneNumber("01011112229")
                .field(1).build();
        User user9 = User.builder()
                .email("user9@gmail.com")
                .pw("123")
                .userName("user9")
                .profile("profile9")
                .hashTagsString("@C++@JAVA@_@축구@")
                .phoneNumber("01011112230")
                .field(1).build();

        id.add(userRepository.save(user1).getId());
        id.add(userRepository.save(user2).getId());
        id.add(userRepository.save(user3).getId());
        id.add(userRepository.save(user4).getId());
        id.add(userRepository.save(user5).getId());
        id.add(userRepository.save(user6).getId());
        id.add(userRepository.save(user7).getId());
        id.add(userRepository.save(user8).getId());
        id.add(userRepository.save(user9).getId());


        Tap newTap1 = Tap.builder().sendUser(user1).receiveUser(user2).msg("gd~").status(0).build();
        Tap newTap2 = Tap.builder().sendUser(user1).receiveUser(user3).msg("gd~").status(0).build();
        Tap newTap3 = Tap.builder().sendUser(user1).receiveUser(user4).msg("gd~").status(0).build();
        Tap newTap4 = Tap.builder().sendUser(user3).receiveUser(user4).msg("gd~").status(0).build();

        newTap1.setInsertDt(LocalDateTime.now());
        tapRepository.save(newTap1);
        Thread.sleep(500);
        newTap2.setInsertDt(LocalDateTime.now());
        tapRepository.save(newTap2);
        Thread.sleep(500);
        newTap3.setInsertDt(LocalDateTime.now());
        tapRepository.save(newTap3);
        Thread.sleep(500);
        newTap4.setInsertDt(LocalDateTime.now());
        tapRepository.save(newTap4);

        Friend friend1 = Friend.builder()
                .me(user7)
                .you(user1)
                .roomId("room1")
                .build();
        Friend friend2 = Friend.builder()
                .me(user7)
                .you(user2)
                .roomId("room2")
                .build();
        Friend friend3 = Friend.builder()
                .me(user7)
                .you(user3)
                .roomId("room3")
                .build();
        Friend friend4 = Friend.builder()
                .me(user7)
                .you(user4)
                .roomId("room4")
                .build();
        Friend friend5 = Friend.builder()
                .me(user7)
                .you(user5)
                .roomId("room5")
                .build();
        Friend friend6 = Friend.builder()
                .me(user7)
                .you(user6)
                .roomId("room6")
                .build();
        Friend friend7 = Friend.builder()
                .me(user7)
                .you(user8)
                .roomId("room7")
                .build();
        Friend friend8 = Friend.builder()
                .me(user7)
                .you(user9)
                .roomId("room8")
                .build();

        friendRepository.save(friend1);
        friendRepository.save(friend2);
        friendRepository.save(friend3);
        friendRepository.save(friend4);
        friendRepository.save(friend5);
        friendRepository.save(friend6);
        friendRepository.save(friend7);
        friendRepository.save(friend8);

    }

    @Test
    @DisplayName("나한테 탭 보낸 유저 정보조회")
    void myreceivetap_userinfo() {
        User user1 = userRepository.findById(id.get(0)).orElse(null);
        User user2 = userRepository.findById(id.get(1)).orElse(null);
        User user3 = userRepository.findById(id.get(2)).orElse(null);
        User user4 = userRepository.findById(id.get(3)).orElse(null);

        List<UserTapDto> userTapDtoList1 = userRepository.findMysendORreceiveTapUserInfo(user2.getId(),1,0);
        assertEquals(userTapDtoList1.size(),1);
        assertEquals(userTapDtoList1.get(0).getUserId(),user1.getId());
        assertEquals(userTapDtoList1.get(0).getEmail(),user1.getEmail());
        assertEquals(userTapDtoList1.get(0).getProfile(),user1.getProfile());
        assertEquals(userTapDtoList1.get(0).getUserName(),user1.getUserName());
        assertEquals(userTapDtoList1.get(0).getHashTags(),user1.getHashTagsString());
        assertEquals(userTapDtoList1.get(0).getField(),user1.getField());

        List<UserTapDto> userTapDtoList2 = userRepository.findMysendORreceiveTapUserInfo(user3.getId(),1,0);
        assertEquals(userTapDtoList2.size(),1);
        assertEquals(userTapDtoList2.get(0).getUserId(),user1.getId());
        assertEquals(userTapDtoList2.get(0).getEmail(),user1.getEmail());
        assertEquals(userTapDtoList2.get(0).getProfile(),user1.getProfile());
        assertEquals(userTapDtoList2.get(0).getUserName(),user1.getUserName());
        assertEquals(userTapDtoList2.get(0).getHashTags(),user1.getHashTagsString());
        assertEquals(userTapDtoList2.get(0).getField(),user1.getField());

        List<UserTapDto> userTapDtoList3 = userRepository.findMysendORreceiveTapUserInfo(user4.getId(),1,0);
        assertEquals(userTapDtoList3.size(),2);
        assertEquals(userTapDtoList3.get(1).getUserId(),user1.getId());
        assertEquals(userTapDtoList3.get(1).getEmail(),user1.getEmail());
        assertEquals(userTapDtoList3.get(1).getProfile(),user1.getProfile());
        assertEquals(userTapDtoList3.get(1).getUserName(),user1.getUserName());
        assertEquals(userTapDtoList3.get(1).getHashTags(),user1.getHashTagsString());
        assertEquals(userTapDtoList3.get(1).getField(),user1.getField());

        assertEquals(userTapDtoList3.get(0).getUserId(),user3.getId());
        assertEquals(userTapDtoList3.get(0).getEmail(),user3.getEmail());
        assertEquals(userTapDtoList3.get(0).getProfile(),user3.getProfile());
        assertEquals(userTapDtoList3.get(0).getUserName(),user3.getUserName());
        assertEquals(userTapDtoList3.get(0).getHashTags(),user3.getHashTagsString());
        assertEquals(userTapDtoList3.get(0).getField(),user3.getField());

        List<UserTapDto> userTapDtoList4 = userRepository.findMysendORreceiveTapUserInfo(user1.getId(),1,0);
        assertEquals(userTapDtoList4.size(),0);
    }

    @Test
    @DisplayName("나한테 탭 받은 유저 정보조회")
    void mysendtap_userinfo() {
        User user1 = userRepository.findById(id.get(0)).orElse(null);
        User user2 = userRepository.findById(id.get(1)).orElse(null);
        User user3 = userRepository.findById(id.get(2)).orElse(null);
        User user4 = userRepository.findById(id.get(3)).orElse(null);

        List<UserTapDto> userTapDtoList1 = userRepository.findMysendORreceiveTapUserInfo(user1.getId(),0,0);
        assertEquals(userTapDtoList1.size(),3);
        assertEquals(userTapDtoList1.get(0).getUserId(),user4.getId());
        assertEquals(userTapDtoList1.get(0).getEmail(),user4.getEmail());
        assertEquals(userTapDtoList1.get(0).getProfile(),user4.getProfile());
        assertEquals(userTapDtoList1.get(0).getUserName(),user4.getUserName());
        assertEquals(userTapDtoList1.get(0).getHashTags(),user4.getHashTagsString());
        assertEquals(userTapDtoList1.get(0).getField(),user4.getField());

        assertEquals(userTapDtoList1.get(1).getUserId(),user3.getId());
        assertEquals(userTapDtoList1.get(1).getEmail(),user3.getEmail());
        assertEquals(userTapDtoList1.get(1).getProfile(),user3.getProfile());
        assertEquals(userTapDtoList1.get(1).getUserName(),user3.getUserName());
        assertEquals(userTapDtoList1.get(1).getHashTags(),user3.getHashTagsString());
        assertEquals(userTapDtoList1.get(1).getField(),user3.getField());

        assertEquals(userTapDtoList1.get(2).getUserId(),user2.getId());
        assertEquals(userTapDtoList1.get(2).getEmail(),user2.getEmail());
        assertEquals(userTapDtoList1.get(2).getProfile(),user2.getProfile());
        assertEquals(userTapDtoList1.get(2).getUserName(),user2.getUserName());
        assertEquals(userTapDtoList1.get(2).getHashTags(),user2.getHashTagsString());
        assertEquals(userTapDtoList1.get(2).getField(),user2.getField());

        List<UserTapDto> userTapDtoList2 = userRepository.findMysendORreceiveTapUserInfo(user3.getId(),0,0);
        assertEquals(userTapDtoList2.size(),1);
        assertEquals(userTapDtoList2.get(0).getUserId(),user4.getId());
        assertEquals(userTapDtoList2.get(0).getEmail(),user4.getEmail());
        assertEquals(userTapDtoList2.get(0).getProfile(),user4.getProfile());
        assertEquals(userTapDtoList2.get(0).getUserName(),user4.getUserName());
        assertEquals(userTapDtoList2.get(0).getHashTags(),user4.getHashTagsString());
        assertEquals(userTapDtoList2.get(0).getField(),user4.getField());
        List<UserTapDto> userTapDtoList3 = userRepository.findMysendORreceiveTapUserInfo(user2.getId(),0,0);
        assertEquals(userTapDtoList3.size(),0);
        List<UserTapDto> userTapDtoList4 = userRepository.findMysendORreceiveTapUserInfo(user4.getId(),0,0);
        assertEquals(userTapDtoList4.size(),0);
    }

    @Test
    @DisplayName("내친구 정보 조회")
    void myfriends(){
        List<String> rooms = new ArrayList<>();
        for(int i = 1 ; i<9 ; i++)
            rooms.add(String.format("room{}",i));
        List<UserTapDto> retDtos = userRepository.findMyFriendsById(id.get(6),rooms);
        for(UserTapDto retDto : retDtos)
        {
            assertTrue(id.contains(retDto.getUserId()));
            User user = userRepository.findById(retDto.getUserId()).orElse(null);
            assertEquals(retDto.getField(),user.getField());
            assertEquals(retDto.getEmail(),user.getEmail());
            assertEquals(retDto.getUserName(),user.getUserName());
            assertEquals(retDto.getHashTags(),user.getHashTagsString());
            assertEquals(retDto.getProfile(),user.getProfile());
        }

    }

    @Test
    @DisplayName("검색 기능")
    void search(){
        SearchRequestDto newSearchRequestDto1 = new SearchRequestDto();
        newSearchRequestDto1.setType(0);
        newSearchRequestDto1.setPage(0);
        newSearchRequestDto1.setField(0);
        List<String> tags = new ArrayList<>();
        tags.add("축구");
        newSearchRequestDto1.setSearchTags(tags);
        List<UserMainDto> ans = userRepository.findAllByTag(newSearchRequestDto1);
        assertEquals(ans.size(),1);
        assertEquals(ans.get(0).getUserId(),id.get(0));

        SearchRequestDto newSearchRequestDto2 = new SearchRequestDto();
        newSearchRequestDto2.setType(0);
        newSearchRequestDto2.setPage(0);
        newSearchRequestDto2.setField(1);
        List<String> tags2 = new ArrayList<>();
        tags2.add("C");
        tags2.add("축구");
        List<Long> rets = new ArrayList<>();
        rets.add(id.get(1));
        rets.add(id.get(4));
        rets.add(id.get(5));
        rets.add(id.get(6));
        rets.add(id.get(7));
        rets.add(id.get(8));
        newSearchRequestDto2.setSearchTags(tags2);
        List<UserMainDto> ans2 = userRepository.findAllByTag(newSearchRequestDto2);
        assertEquals(ans2.size(),6);
        for(UserMainDto an : ans2)
        {
            assertTrue(rets.contains(an.getUserId()));
        }


    }

    @Test
    @DisplayName("휴대폰번호 사용하는사람있는지..")
    void searchPhone(){
        assertTrue(userRepository.existUserByPhoneNumber("01011112222"));
        assertTrue(userRepository.existUserByPhoneNumber("01011112223"));
        assertTrue(userRepository.existUserByPhoneNumber("01011112224"));
        assertTrue(userRepository.existUserByPhoneNumber("01011112225"));
        assertTrue(userRepository.existUserByPhoneNumber("01011112226"));
        assertTrue(userRepository.existUserByPhoneNumber("01011112227"));
        assertTrue(userRepository.existUserByPhoneNumber("01011112228"));
        assertTrue(userRepository.existUserByPhoneNumber("01011112229"));
        assertTrue(userRepository.existUserByPhoneNumber("01011112230"));
        assertFalse(userRepository.existUserByPhoneNumber("01011112231"));
        assertFalse(userRepository.existUserByPhoneNumber("01011112130"));
        assertFalse(userRepository.existUserByPhoneNumber("01011114230"));
        assertFalse(userRepository.existUserByPhoneNumber("01011312230"));
        assertFalse(userRepository.existUserByPhoneNumber("010111312230"));
    }

    @Test
    @DisplayName("유저네임 사용하는 사람있는지.//")
    void searchName(){
        assertTrue(userRepository.existUserByUserName("user1"));
        assertTrue(userRepository.existUserByUserName("user2"));
        assertTrue(userRepository.existUserByUserName("user3"));
        assertTrue(userRepository.existUserByUserName("user4"));
        assertTrue(userRepository.existUserByUserName("user5"));
        assertTrue(userRepository.existUserByUserName("user6"));
        assertTrue(userRepository.existUserByUserName("user7"));
        assertTrue(userRepository.existUserByUserName("user8"));
        assertTrue(userRepository.existUserByUserName("user9"));
        assertFalse(userRepository.existUserByUserName("user10"));
        assertFalse(userRepository.existUserByUserName("use4r"));
        assertFalse(userRepository.existUserByUserName("zfas"));
        assertFalse(userRepository.existUserByUserName("user1234"));
        assertFalse(userRepository.existUserByUserName("user"));
    }

}