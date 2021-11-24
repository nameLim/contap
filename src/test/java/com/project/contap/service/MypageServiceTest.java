package com.project.contap.service;

import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.common.util.ImageService;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.card.Card;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.card.dto.BackRequestCardDto;
import com.project.contap.model.card.dto.BackResponseCardDto;
import com.project.contap.model.hashtag.HashTagRepositoty;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.FrontRequestCardDto;
import com.project.contap.model.user.dto.FrontResponseCardDto;
import com.project.contap.model.user.dto.UserInfoDto;
import com.project.contap.security.UserDetailsImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

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
            .profile("")
            .cards(new ArrayList<>())
            .userStatus(UserStatusEnum.ACTIVE).build();

    UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);

    @Nested
    @DisplayName("마이페이지 유저정보 조회")
    class getMyInfo {
        @Test
        @DisplayName("성공")
        void getMyInfo_success() {
            when(userService.userFromUserDetails(any(UserDetails.class)))
                    .thenReturn(testUser);
            UserInfoDto infoDto = mypageService.getMyInfo(testUser);
            assertEquals("testUser", infoDto.getUserName());
        }

        @Test
        @DisplayName("실패_user가 null일 경우")
        void getMyInfo_fail() {
//            given(userService.userFromUserDetails(null))
//                    .willThrow(new ContapException(ErrorCode.USER_NOT_FOUND));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.userFromUserDetails(null);
            });
            assertEquals(exception.getErrorCode(), ErrorCode.USER_NOT_FOUND);
        }
    }

    @Nested
    @DisplayName("마이페이지 앞면 카드 수정")
    class modifyFrontCard {
        @Test
        @DisplayName("성공")
        void modifyFrontCard_success() throws IOException {
            when(userService.userFromUserDetails(any(UserDetails.class)))
                    .thenReturn(testUser);

            FrontRequestCardDto frontRequestCardDto = new FrontRequestCardDto(null, "김혜림", "Java", 0);
            when(userRepository.existUserByUserName(frontRequestCardDto.getUserName())).thenReturn(false);
            when(userRepository.save(testUser)).thenReturn(testUser);
            FrontResponseCardDto frontCardDto = mypageService.modifyFrontCard(frontRequestCardDto, testUser);
            assertEquals(frontCardDto.getHashTagsString(), "@_@"); //hash_tag 테이블에 아무값이 없으므로 예상값은 @_@임
        }

        @Test
        @DisplayName("실패_nickname 변경했을 때 중복 됐을 경우")
        void modifyFrontCard_fail() throws IOException {
            when(userService.userFromUserDetails(any(UserDetails.class)))
                    .thenReturn(testUser);
            FrontRequestCardDto frontRequestCardDto = new FrontRequestCardDto(null, "김혜림", "Java", 0);
            when(userRepository.existUserByUserName(frontRequestCardDto.getUserName())).thenReturn(true);

            ContapException exception = assertThrows(ContapException.class, () -> {
                mypageService.modifyFrontCard(frontRequestCardDto, testUser);
            });
            assertEquals(exception.getErrorCode(), ErrorCode.NICKNAME_DUPLICATE);
        }
    }

    @Nested
    @DisplayName("마이페이지 뒷면 카드 생성")
    class createBackCard {
        @Test
        @DisplayName("성공")
        void createBackCard_success() {
            when(userService.userFromUserDetails(any(UserDetails.class)))
                    .thenReturn(testUser);
            BackRequestCardDto backRequestCardDto = BackRequestCardDto.builder()
                    .title("뒷면카드 제목")
                    .content("뒷면카드 내용")
                    .tagsStr("카드해쉬태그")
                    .link("").build();
            Card card = Card.builder().build();
            when(cardRepository.save(card)).thenReturn(card);
            BackResponseCardDto backResponseCardDto = mypageService.createBackCard(backRequestCardDto, testUser);
            assertEquals(backResponseCardDto.getTitle(), backRequestCardDto.getTitle());
        }

        @Test
        @DisplayName("실패_카드수가 10장 이상일 경우")
        void createBackCard_fail() {
            when(userService.userFromUserDetails(any(UserDetails.class)))
                    .thenReturn(testUser);

            BackRequestCardDto backDto = BackRequestCardDto.builder()
                    .title("뒷면카드 제목")
                    .content("뒷면카드 내용")
                    .tagsStr("카드해쉬태그")
                    .link("").build();

            if (testUser.getCards().size() >= 10) {
                ContapException exception = assertThrows(ContapException.class, () -> {
                    mypageService.createBackCard(backDto, testUser);
                });
                assertEquals(exception.getErrorCode(), ErrorCode.EXCESS_CARD_MAX);
            }
        }
    }

    @Nested
    @DisplayName("마이페이지 뒷면 카드 수정")
    class modifyBackCard {
        @Test
        @DisplayName("성공")
        void modifyBackCard_success() {
            when(userService.userFromUserDetails(any(UserDetails.class)))
                    .thenReturn(testUser);
            BackRequestCardDto backRequestCardDto = BackRequestCardDto.builder()
                    .title("뒷면카드 제목")
                    .content("뒷면카드 내용")
                    .tagsStr("카드해쉬태그")
                    .link("").build();
            Card card = Card.builder().build();
            when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
            when(cardRepository.save(card)).thenReturn(card);

            BackResponseCardDto backResponseCardDto = mypageService.modifyBackCard(1L, backRequestCardDto, testUser);
            assertEquals(backResponseCardDto.getTitle(), backRequestCardDto.getTitle());
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