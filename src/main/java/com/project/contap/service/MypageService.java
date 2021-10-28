package com.project.contap.service;

import com.project.contap.dto.CardDto;
import com.project.contap.dto.UserFrontCardDto;
import com.project.contap.dto.UserInfoDto;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.model.User;
import com.project.contap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MypageService {

    private final UserRepository userRepository;

    // 회원 정보 가져오기
    // 가져오는 값 : 기본 회원정보(앞면카드), 모든 뒷면카드
    public UserInfoDto getMyInfo(User requestUser) {

        if( requestUser == null) {
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.
        }

        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if(!user.getEmail().equals(requestUser.getEmail()))
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.

        // Hash Tag
        List<HashTag> userHashTags = user.getTags() != null ? user.getTags() : new ArrayList<>();
        List<String> stackHashTags = new ArrayList<>();
        List<String> interestHashTags = new ArrayList<>();

        for (HashTag hashTag : userHashTags) {
            int type = hashTag.getType();
            if (type == 0) // stack
                stackHashTags.add(hashTag.getName());
            else if (type == 1) // interest
                interestHashTags.add(hashTag.getName());
            else {
                throw new ContapException(ErrorCode.WRONG_HASHTAG_TYPE); //잘못된 해쉬태그타입이 존재합니다. 관리자에게 문의하세요.
            }
        }

        //Card
        List<Card> userCards = user.getCards();
        List<CardDto> cardDtoList = new ArrayList<>();

        for(Card card: userCards) {
            CardDto cardDto = CardDto.builder()
                            .cardId(card.getId())
                            .title(card.getTitle())
                            .content(card.getContent())
                            .stackHashTags(stackHashTags)
                            .interestHashTags(interestHashTags).build();
            cardDtoList.add(cardDto);
        }

        UserInfoDto userInfoDto = UserInfoDto.builder()
                                                .userId(user.getId())
                                                .userName(user.getUserName())
                                                .profile(user.getProfile())
                                                .stackHashTags(stackHashTags)
                                                .interestHashTags(interestHashTags)
                                                .cardDtoList(cardDtoList).build();

        return userInfoDto;
    }

    //앞면 카드 정보 수정
    public UserFrontCardDto modifyFrontCard(UserFrontCardDto userFrontCardDto, User requestUser){
            // 변경할 수 있는 값 : profile, userName, hashTags

        if (requestUser == null) {
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.
        }

        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if (!user.getEmail().equals(requestUser.getEmail()))
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.


        List<HashTag> hashTagList = new ArrayList<>(); // user에  넣을 해쉬태그 리스트
        List<String> tagStringList;
        StringBuilder hashTagStr = new StringBuilder();

        //stackTags
        tagStringList = userFrontCardDto.getStackHashTags();
        if(tagStringList.size() > 0) {
            for(String str: userFrontCardDto.getStackHashTags()) {
                hashTagList.add(new HashTag(str, 0));
                hashTagStr.append("#"+str);
            }
        }

        //interestTags
        tagStringList = userFrontCardDto.getInterestHashTags();
        if(tagStringList.size() > 0) {
            hashTagStr.append("_");
            for(String str: tagStringList) {
                hashTagList.add(new HashTag(str, 1));
                hashTagStr.append("#"+str);
            }
        }

        //user 값 넣기
        user.setProfile(userFrontCardDto.getProfile());
        user.setUserName(userFrontCardDto.getUserName());
        user.setTags(hashTagList);
        user.setHashTagsString(hashTagStr.toString());
        userRepository.save(user);

        return userFrontCardDto;
    }
}
