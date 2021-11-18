package com.project.contap.service;

import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.common.util.ImageService;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MypageServiceTest {

    @InjectMocks
    private MypageService mypageService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private HashTagRepositoty hashTagRepositoty;
    @Mock
    private ImageService imageService;
    @Mock
    private UserService userService;

    User testUser = User.builder()
            .email("testUser@gmail.com")
            .pw("1234qwer")
            .userName("testUser")
            .userStatus(UserStatusEnum.ACTIVE).build();


    @Nested
    @DisplayName("마이페이지 유저정보 조회")
    class getMyInfo {
        @Test
        @DisplayName("성공")
        void getMyInfo_success() {

        }

        @Test
        @DisplayName("실패")
        void getMyInfo_fail() {

        }
    }

    @Nested
    @DisplayName("마이페이지 앞면 카드 변경")
    class modifyFrontCard {
        @Test
        @DisplayName("성공")
        void modifyFrontCard_success() {

        }

        @Test
        @DisplayName("실패")
        void modifyFrontCard_fail() {

        }
    }

    @Nested
    @DisplayName("마이페이지 뒷면 카드 생성")
    class createBackCard {
        @Test
        @DisplayName("성공")
        void createBackCard_success() {

        }

        @Test
        @DisplayName("실패")
        void createBackCard_fail() {

        }
    }

    @Nested
    @DisplayName("마이페이지 뒷면 카드 변경")
    class modifyBackCard {
        @Test
        @DisplayName("성공")
        void modifyBackCard_success() {

        }

        @Test
        @DisplayName("실패")
        void modifyBackCard_fail() {

        }
    }

    @Nested
    @DisplayName("마이페이지 뒷면 카드 삭제")
    class deleteBackCard {
        @Test
        @DisplayName("성공")
        void deleteBackCard_success() {

        }

        @Test
        @DisplayName("실패")
        void deleteBackCard_fail() {

        }
    }
}