package com.project.contap.service;

import com.project.contap.common.Common;
import com.project.contap.common.DefaultRsp;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.friend.FriendRepository;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MainServiceTest {

    @Mock
    HashTagRepositoty hashTagRepositoty;
    @Mock
    UserRepository userRepository;
    @Mock
    TapRepository tapRepository;
    @Mock
    FriendRepository friendRepository;
    @Mock
    CardRepository cardRepository;
    @Mock
    UserService userService;
    @Mock
    Common common;

    Long tapId = 1L;
    String receiveUserEmail = "lsj2867@gmail.com";
    User imsiSender = User.builder()
            .id(1L)
            .email("lsj2867@gmail.com")
            .pw("123")
            .userName("lsj2867")
            .field(0).build();
    User imsiReceiver = User.builder()
            .id(2L)
            .email("lsj4534@gmail.com")
            .pw("123")
            .userName("lsj4534")
            .field(1).build();


    @Test
    @DisplayName("탭요청 - 정상처리")
    void dotap_good() {
        Tap mockTap = null;
        MainService mainService = new MainService(hashTagRepositoty,userRepository,tapRepository,friendRepository,cardRepository,userService,common);
        when(userRepository.findById(imsiReceiver.getId()))
                .thenReturn(Optional.of(imsiReceiver));
        when(friendRepository.checkFriend(imsiSender,imsiReceiver))
                .thenReturn(false);
        when(tapRepository.checkReceiveTap(imsiReceiver,imsiSender))
                .thenReturn(mockTap);
        when(tapRepository.checkSendTap(imsiReceiver,imsiSender))
                .thenReturn(false);
        DefaultRsp dfRsp = mainService.dotap(imsiSender,imsiReceiver.getId(),"gd");
        assertEquals("정상적으로 처리 되었습니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("탭요청 - 자신한테 탭요청")
    void dotap_sendto() {
        MainService mainService = new MainService(hashTagRepositoty,userRepository,tapRepository,friendRepository,cardRepository,userService,common);
        DefaultRsp dfRsp = mainService.dotap(imsiSender,imsiSender.getId(),"gd");
        assertEquals("자신한테 탭요청하지못해요", dfRsp.getMsg());
    }

    @Test
    @DisplayName("탭요청 - 이미 친구 관계")
    void dotap_alreadyfriend() {
        MainService mainService = new MainService(hashTagRepositoty,userRepository,tapRepository,friendRepository,cardRepository,userService,common);
        when(userRepository.findById(imsiReceiver.getId()))
                .thenReturn(Optional.of(imsiReceiver));
        when(friendRepository.checkFriend(imsiSender,imsiReceiver))
                .thenReturn(true);
        DefaultRsp dfRsp = mainService.dotap(imsiSender,imsiReceiver.getId(),"gd");
        assertEquals("이미 친구 관계입니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("탭요청 - 이미 상대한테 요청을 보낸 상태")
    void dotap_alreadysend() {
//        Tap mockTap = Tap.builder().id(1L).sendUser(imsiSender).receiveUser(imsiReceiver).msg("gd").status(0).build();
        Tap mockTap = null;
        MainService mainService = new MainService(hashTagRepositoty,userRepository,tapRepository,friendRepository,cardRepository,userService,common);
        when(userRepository.findById(imsiReceiver.getId()))
                .thenReturn(Optional.of(imsiReceiver));
        when(friendRepository.checkFriend(imsiSender,imsiReceiver))
                .thenReturn(false);
        when(tapRepository.checkReceiveTap(imsiReceiver,imsiSender))
                .thenReturn(mockTap);
        when(tapRepository.checkSendTap(imsiReceiver,imsiSender))
                .thenReturn(true);
        DefaultRsp dfRsp = mainService.dotap(imsiSender,imsiReceiver.getId(),"gd");
        assertEquals("이미 상대에게 요청을 보낸 상태입니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("탭요청 - 상대도 나한테 요청을 보낸 상태")
    void dotap_receiversendme() {
        Tap mockTap = Tap.builder().id(1L).sendUser(imsiSender).receiveUser(imsiReceiver).msg("gd").status(0).build();
        MainService mainService = new MainService(hashTagRepositoty,userRepository,tapRepository,friendRepository,cardRepository,userService,common);
        when(userRepository.findById(imsiReceiver.getId()))
                .thenReturn(Optional.of(imsiReceiver));
        when(friendRepository.checkFriend(imsiSender,imsiReceiver))
                .thenReturn(false);
        when(tapRepository.checkReceiveTap(imsiReceiver,imsiSender))
                .thenReturn(mockTap);
        DefaultRsp dfRsp = mainService.dotap(imsiSender,imsiReceiver.getId(),"gd");
        assertEquals("상대방의 요청을 수락 하였습니다.", dfRsp.getMsg());
    }

    @Test
    void tutorial() {
    }
}