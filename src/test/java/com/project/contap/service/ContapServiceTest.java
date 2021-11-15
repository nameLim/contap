package com.project.contap.service;

import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.common.Common;
import com.project.contap.common.DefaultRsp;
import com.project.contap.model.tap.Tap;
import com.project.contap.model.tap.TapRepository;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ContapServiceTest {

    @Mock
    TapRepository tapRepository;
    @Mock
    ChatRoomRepository chatRoomRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    Common common;

    Long tapId = 1L;
    String receiveUserEmail = "lsj2867@gmail.com";
    User imsiSender = User.builder()
            .email("lsj2867@gmail.com")
            .pw("123")
            .userName("lsj2867")
            .field(0).build();
    User imsiReceiver = User.builder()
            .email("lsj4534@gmail.com")
            .pw("123")
            .userName("lsj4534")
            .field(1).build();
    @Test
    @DisplayName("Tap 거절 - 정상처리")
    void tapReject_good() {
        Tap mockTap = Tap.builder().id(tapId).sendUser(imsiSender).receiveUser(imsiReceiver).msg("gd").status(0).build();
        ContapService cantapService = new ContapService(tapRepository,chatRoomRepository,userRepository,common);
        when(tapRepository.findById(tapId))
                .thenReturn(Optional.of(mockTap));
        DefaultRsp dfRsp = cantapService.tapReject(tapId,imsiReceiver.getEmail());
        assertEquals("정상적으로 처리 되었습니다.", dfRsp.getMsg());

    }

    @Test
    @DisplayName("Tap 거절 - 이미처리된탭_status_1")
    void tapReject_already() {
        Tap mockTap = Tap.builder().id(tapId).sendUser(imsiSender).receiveUser(imsiReceiver).msg("gd").status(1).build();
        ContapService cantapService = new ContapService(tapRepository,chatRoomRepository,userRepository,common);
        when(tapRepository.findById(tapId))
                .thenReturn(Optional.of(mockTap));
        DefaultRsp dfRsp = cantapService.tapReject(tapId,imsiReceiver.getEmail());
        assertEquals("이미 처리된 Tap 입니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("Tap 거절 - 이미처리된탭_status_2")
    void tapReject_already2() {
        Tap mockTap = Tap.builder().id(tapId).sendUser(imsiSender).receiveUser(imsiReceiver).msg("gd").status(2).build();
        ContapService cantapService = new ContapService(tapRepository,chatRoomRepository,userRepository,common);
        when(tapRepository.findById(tapId))
                .thenReturn(Optional.of(mockTap));
        DefaultRsp dfRsp = cantapService.tapReject(tapId,imsiReceiver.getEmail());
        assertEquals("이미 처리된 Tap 입니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("Tap 거절 - 해당탭을 못찾은경우")
    void tapReject_notfound() {
        Tap mockTap = null;
        ContapService cantapService = new ContapService(tapRepository,chatRoomRepository,userRepository,common);
        when(tapRepository.findById(tapId))
                .thenReturn(Optional.ofNullable(mockTap));
        DefaultRsp dfRsp = cantapService.tapReject(tapId,imsiReceiver.getEmail());
        assertEquals("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..", dfRsp.getMsg());
    }

    @Test
    @DisplayName("Tap 수락 - 정상처리")
    void tapAccept_good() {
        Tap mockTap = Tap.builder().id(tapId).sendUser(imsiSender).receiveUser(imsiReceiver).msg("gd").status(0).build();
        ContapService cantapService = new ContapService(tapRepository,chatRoomRepository,userRepository,common);
        when(tapRepository.findById(tapId))
                .thenReturn(Optional.of(mockTap));
        DefaultRsp dfRsp = cantapService.rapAccept(tapId,imsiReceiver.getEmail());
        assertEquals("정상적으로 처리 되었습니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("Tap 수락 - 이미처리된탭_status_1")
    void tapAccept_already() {
        Tap mockTap = Tap.builder().id(tapId).sendUser(imsiSender).receiveUser(imsiReceiver).msg("gd").status(1).build();
        ContapService cantapService = new ContapService(tapRepository,chatRoomRepository,userRepository,common);
        when(tapRepository.findById(tapId))
                .thenReturn(Optional.of(mockTap));
        DefaultRsp dfRsp = cantapService.rapAccept(tapId,imsiReceiver.getEmail());
        assertEquals("이미 처리된 Tap 입니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("Tap 수락 - 이미처리된탭_status_2")
    void tapAccept_already2() {
        Tap mockTap = Tap.builder().id(tapId).sendUser(imsiSender).receiveUser(imsiReceiver).msg("gd").status(2).build();
        ContapService cantapService = new ContapService(tapRepository,chatRoomRepository,userRepository,common);
        when(tapRepository.findById(tapId))
                .thenReturn(Optional.of(mockTap));
        DefaultRsp dfRsp = cantapService.rapAccept(tapId,imsiReceiver.getEmail());
        assertEquals("이미 처리된 Tap 입니다.", dfRsp.getMsg());
    }

    @Test
    @DisplayName("Tap 수락 - 해당탭을 못찾은경우")
    void tapAccept_notfound() {
        Tap mockTap = null;
        ContapService cantapService = new ContapService(tapRepository,chatRoomRepository,userRepository,common);
        when(tapRepository.findById(tapId))
                .thenReturn(Optional.ofNullable(mockTap));
        DefaultRsp dfRsp = cantapService.rapAccept(tapId,imsiReceiver.getEmail());
        assertEquals("해당 tab이 존재하지 않습니다 TabID를 다시확인해주세요..", dfRsp.getMsg());
    }
}