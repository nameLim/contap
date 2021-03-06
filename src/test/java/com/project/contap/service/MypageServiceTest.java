package com.project.contap.service;

import com.project.contap.common.enumlist.UserStatusEnum;
import com.project.contap.common.util.ImageService;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.card.Card;
import com.project.contap.model.card.CardRepository;
import com.project.contap.model.card.dto.BackRequestCardDto;
import com.project.contap.model.card.dto.BackResponseCardDto;
import com.project.contap.model.hashtag.HashTag;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
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
    @DisplayName("??????????????? ???????????? ??????")
    class getMyInfo {
        @Test
        @DisplayName("??????")
        void getMyInfo_success() {
            User user = User.builder().id(999999L).userName("?????????12").pw("pw").email("email").profile("profile").build();
            List<Card> cards = new ArrayList<>();
            Long testSize = 8L;
            for(Long i = 0L ; i < testSize ; i++)
                cards.add(Card.builder()
                        .id(i)
                        .user(user)
                        .cardOrder(i)
                        .title(String.format("title%d",i))
                        .content(String.format("content%d",i))
                        .link(String.format("link%d",i))
                        .tagsString(String.format("tagsString%d",i)).build());
            when(cardRepository.findAllByUser(user)).thenReturn(cards);

            UserInfoDto infoDto = mypageService.getMyInfo(user);
            assertEquals(user.getId(), infoDto.getUserId());
            assertEquals(user.getPw(), infoDto.getPassword());
            assertEquals(user.getUserName(), infoDto.getUserName());
            assertEquals(user.getProfile(), infoDto.getProfile());
            assertEquals(user.getField(), infoDto.getField());
            assertEquals(user.getAuthStatus(), infoDto.getAuthStatus());
            assertEquals(user.getHashTagsString(), infoDto.getHashTagsString());
            assertEquals(testSize, infoDto.getCardDtoList().size());
            for(int i = 0 ; i < testSize ; i++)
            {
                assertEquals(cards.get(i).getId(), infoDto.getCardDtoList().get(i).getCardId());
                assertEquals(cards.get(i).getUser().getId(), infoDto.getCardDtoList().get(i).getUserId());
                assertEquals(cards.get(i).getTitle(), infoDto.getCardDtoList().get(i).getTitle());
                assertEquals(cards.get(i).getContent(), infoDto.getCardDtoList().get(i).getContent());
                assertEquals(cards.get(i).getTagsString(), infoDto.getCardDtoList().get(i).getTagsStr());
                assertEquals(cards.get(i).getLink(), infoDto.getCardDtoList().get(i).getLink());
                assertEquals(cards.get(i).getUser().getField(), infoDto.getCardDtoList().get(i).getField());
            }

        }
    }

    @Nested
    @DisplayName("??????????????? ?????? ?????? ??????")
    class modifyFrontCard {
        @Test
        @DisplayName("?????? profile null, tag Java")
        void modifyFrontCard_success_1() throws IOException {
            User user = User.builder().id(999999L).userName("?????????12").pw("pw").email("email").profile("profile").build();
            Set<String> sets = new HashSet<>();
            sets.add("Java");
            List<HashTag> hashtags = new ArrayList<>();
            hashtags.add(new HashTag(1L,"Java",0));
            FrontRequestCardDto frontRequestCardDto = new FrontRequestCardDto(null,
                    "?????????",
                    "Java",
                    0);
            when(userRepository.existUserByUserName(frontRequestCardDto.getUserName())).thenReturn(false);
            when(hashTagRepositoty.findAllByNameIn(sets)).thenReturn(hashtags);
            when(userRepository.save(user)).thenReturn(user);
            FrontResponseCardDto frontCardDto = mypageService.modifyFrontCard(frontRequestCardDto, user);

            assertEquals(frontCardDto.getHashTagsString(), "@Java@_@");
            assertEquals(frontCardDto.getProfile(), "profile");
            assertEquals(frontCardDto.getUserName(), "?????????");
        }

        @Test
        @DisplayName("?????? profile null, tag ?????? , ???????????? ????????????")
        void modifyFrontCard_success_2() throws IOException {
            User user = User.builder().id(999999L).userName("?????????12").pw("pw").email("email").profile("profile").build();
            Set<String> sets = new HashSet<>();
            sets.add("??????");
            List<HashTag> hashtags = new ArrayList<>();
            hashtags.add(new HashTag(1L,"??????",1));
            FrontRequestCardDto frontRequestCardDto = new FrontRequestCardDto(null,
                    "?????????12",
                    "??????",
                    0);
            when(hashTagRepositoty.findAllByNameIn(sets)).thenReturn(hashtags);
            when(userRepository.save(user)).thenReturn(user);
            FrontResponseCardDto frontCardDto = mypageService.modifyFrontCard(frontRequestCardDto, user);

            assertEquals(frontCardDto.getHashTagsString(), "@_@??????@");
            assertEquals(frontCardDto.getProfile(), "profile");
            assertEquals(frontCardDto.getUserName(), "?????????12");
        }

        @Test
        @DisplayName("?????? profile not null, tag Java,??????")
        void modifyFrontCard_success_3() throws IOException {
            User user = User.builder().id(999999L).userName("?????????12").pw("pw").email("email").profile("profile").build();

            Set<String> sets = new HashSet<>();
            sets.add("Java");
            sets.add("??????");

            List<HashTag> hashtags = new ArrayList<>();
            hashtags.add(new HashTag(1L,"Java",0));
            hashtags.add(new HashTag(2L,"??????",1));

            String userPath = System.getProperty("user.dir");
            MultipartFile multipartFile = new MockMultipartFile("test.jpg", new FileInputStream(new File(userPath+ "/src/test/resources/static/test.jpg")));

            FrontRequestCardDto frontRequestCardDto = new FrontRequestCardDto(multipartFile,
                    "?????????",
                    "Java,??????",
                    0);

            when(userRepository.existUserByUserName(frontRequestCardDto.getUserName()))
                    .thenReturn(false);
            when(hashTagRepositoty.findAllByNameIn(sets))
                    .thenReturn(hashtags);
            when(userRepository.save(user))
                    .thenReturn(user);
            when(imageService.upload(multipartFile, "static", user.getProfile()))
                    .thenReturn("testPath");

            FrontResponseCardDto frontCardDto = mypageService.modifyFrontCard(frontRequestCardDto, user);

            assertEquals(frontCardDto.getHashTagsString(), "@Java@_@??????@");
            assertEquals(frontCardDto.getProfile(), "testPath");
            assertEquals(frontCardDto.getUserName(), "?????????");
        }

        @Test
        @DisplayName("??????_nickname ???????????? ??? ?????? ?????? ??????")
        void modifyFrontCard_fail() throws IOException {
            User user = User.builder().id(999999L).userName("?????????12").pw("pw").email("email").profile("profile").build();
            FrontRequestCardDto frontRequestCardDto = new FrontRequestCardDto(null, "?????????", "Java", 0);
            when(userRepository.existUserByUserName(frontRequestCardDto.getUserName())).thenReturn(true);

            ContapException exception = assertThrows(ContapException.class, () -> {
                mypageService.modifyFrontCard(frontRequestCardDto, user);
            });
            assertEquals(exception.getErrorCode(), ErrorCode.NICKNAME_DUPLICATE);
        }
    }

    @Nested
    @DisplayName("??????????????? ?????? ?????? ??????")
    class createBackCard {
        @Test
        @DisplayName("??????")
        void createBackCard_success() {
            User user = User.builder().id(999999L).userName("?????????12").pw("pw").email("email").profile("profile").build();
            BackRequestCardDto backRequestCardDto = BackRequestCardDto.builder()
                    .title("???????????? ??????")
                    .content("???????????? ??????")
                    .tagsStr("??????????????????")
                    .link("link").build();

            Card card = Card.builder()
                    .cardOrder(1L)
                    .user(user)
                    .title(backRequestCardDto.getTitle())
                    .content(backRequestCardDto.getContent())
                    .tagsString(backRequestCardDto.getTagsStr())
                    .link(backRequestCardDto.getLink())
                    .build();
            when(cardRepository.findAllByUser(user)).thenReturn(new ArrayList<>());
            when(cardRepository.save(any(Card.class))).thenReturn(card);
            when(userRepository.save(user)).thenReturn(user);

            BackResponseCardDto backResponseCardDto = mypageService.createBackCard(backRequestCardDto, user);

            assertEquals(backResponseCardDto.getTitle(), backRequestCardDto.getTitle());
            assertEquals(backResponseCardDto.getContent(), backRequestCardDto.getContent());
            assertEquals(backResponseCardDto.getLink(), backRequestCardDto.getLink());
            assertEquals(backResponseCardDto.getTagsStr(), backRequestCardDto.getTagsStr());
            assertEquals(backResponseCardDto.getUserId(), user.getId());
        }

        @Test
        @DisplayName("??????_???????????? 10??? ????????? ??????")
        void createBackCard_fail() {
            User user = User.builder().id(999999L).userName("?????????12").pw("pw").email("email").profile("profile").build();
            List<Card> cards = new ArrayList<>();
            for(int i = 0 ; i<10;i++)
                cards.add(Card.builder().build());
            when(cardRepository.findAllByUser(user)).thenReturn(cards);
            BackRequestCardDto backDto = BackRequestCardDto.builder()
                    .title("???????????? ??????")
                    .content("???????????? ??????")
                    .tagsStr("??????????????????")
                    .link("link").build();

            ContapException exception = assertThrows(ContapException.class, () -> {
                mypageService.createBackCard(backDto, user);
            });
            assertEquals(exception.getErrorCode(), ErrorCode.EXCESS_CARD_MAX);


        }
    }

    @Nested
    @DisplayName("??????????????? ?????? ?????? ??????")
    class modifyBackCard {
        @Test
        @DisplayName("??????")
        void modifyBackCard_success() {
            User user = User.builder().id(999999L).userName("?????????12").pw("pw").email("email").profile("profile").build();
            BackRequestCardDto backRequestCardDto = BackRequestCardDto.builder()
                    .title("???????????? ??????")
                    .content("???????????? ??????")
                    .tagsStr("??????????????????")
                    .link("link").build();
            Card card = Card.builder().user(user).title("???????????????").content("???????????????").tagsString("???????????????").link("???????????????").build();
            Card card2 = Card.builder().user(user).build();
            card2.update(backRequestCardDto);
            when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
            when(cardRepository.save(card)).thenReturn(card2);

            BackResponseCardDto backResponseCardDto = mypageService.modifyBackCard(1L, backRequestCardDto, user);
            assertEquals(backResponseCardDto.getTitle(), backRequestCardDto.getTitle());
            assertEquals(backResponseCardDto.getContent(), backRequestCardDto.getContent());
            assertEquals(backResponseCardDto.getLink(), backRequestCardDto.getLink());
            assertEquals(backResponseCardDto.getTagsStr(), backRequestCardDto.getTagsStr());
            assertEquals(backResponseCardDto.getUserId(), user.getId());

        }

        @Test
        @DisplayName("??????")
        void modifyBackCard_fail() {
            User user = User.builder().id(999999L).userName("?????????12").pw("pw").email("email").profile("profile").build();
            BackRequestCardDto backRequestCardDto = BackRequestCardDto.builder()
                    .title("???????????? ??????")
                    .content("???????????? ??????")
                    .tagsStr("??????????????????")
                    .link("link").build();
            ContapException exception = assertThrows(ContapException.class, () -> {
                mypageService.modifyBackCard(1L,backRequestCardDto, user);
            });
            assertEquals(exception.getErrorCode(), ErrorCode.NOT_FOUND_CARD);
        }
    }

    @Nested
    @DisplayName("??????????????? ?????? ?????? ??????")
    class deleteBackCard {
        @Test
        @DisplayName("??????")
        void deleteBackCard_success() {
            User user = User.builder().id(999999L).userName("?????????12").pw("pw").email("email").profile("profile").build();
            Card card = Card.builder().user(user).title("??????").content("??????").tagsString("??????").link("??????").build();

            when(cardRepository.findById(1L)).thenReturn(Optional.of(card));

            BackResponseCardDto deleteBackCard = mypageService.deleteBackCard(1L, user);
            assertEquals(deleteBackCard.getTitle(), card.getTitle());
            assertEquals(deleteBackCard.getContent(), card.getContent());
            assertEquals(deleteBackCard.getLink(), card.getLink());
            assertEquals(deleteBackCard.getTagsStr(), card.getTagsString());
            assertEquals(deleteBackCard.getUserId(), card.getUser().getId());

        }

        @Test
        @DisplayName("?????? ?????? ?????????")
        void deleteBackCard_fail1() {
            User user = User.builder().id(999999L).userName("?????????12").pw("pw").email("email").profile("profile").build();

            ContapException exception = assertThrows(ContapException.class, () -> {
                mypageService.deleteBackCard(1L, user);
            });
            assertEquals(exception.getErrorCode(), ErrorCode.NOT_FOUND_CARD);
        }

        @Test
        @DisplayName("?????? ?????? ??????")
        void deleteBackCard_fail2() {
            User user = User.builder().id(999999L).userName("?????????12").pw("pw").email("email").profile("profile").build();
            User user2 = User.builder().id(99999L).userName("?????????12").pw("pw").email("email").profile("profile").build();
            Card card = Card.builder().user(user2).title("??????").content("??????").tagsString("??????").link("??????").build();
            when(cardRepository.findById(1L)).thenReturn(Optional.of(card));
            ContapException exception = assertThrows(ContapException.class, () -> {
                mypageService.deleteBackCard(1L, user);
            });
            assertEquals(exception.getErrorCode(), ErrorCode.ACCESS_DENIED);
        }
    }
}