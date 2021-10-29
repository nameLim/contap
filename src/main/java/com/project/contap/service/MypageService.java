package com.project.contap.service;

import com.project.contap.dto.*;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.AuthorityEnum;
import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.model.User;
import com.project.contap.repository.CardRepository;
import com.project.contap.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@RequiredArgsConstructor
@Service
public class MypageService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;

    // 회원 정보 가져오기
    // 가져오는 값 : 기본 회원정보(앞면카드), 모든 뒷면카드
    public UserInfoDto getMyInfo(User requestUser) {

        if( requestUser == null) {
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.
        }

        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if(user.getEmail()!=null && !user.isWritedBy(requestUser))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.

        //Card
        List<Card> userCards = user.getCards();
        List<BackResponseCardDto> cardDtoList = new ArrayList<>();

        for(Card card: userCards) {
            BackResponseCardDto cardDto = BackResponseCardDto.builder()
                                .cardId(card.getId())
                                .userId(card.getUser().getId())
                                .title(card.getTitle())
                                .hashTagsString(card.getHashTagsString())
                                .content(card.getContent()).build();
            cardDtoList.add(cardDto);
        }

        UserInfoDto userInfoDto = UserInfoDto.builder()
                                                .userId(user.getId())
                                                .password(user.getPw())
                                                .userName(user.getUserName())
                                                .profile(user.getProfile())
                                                .authorityEnum(user.getAuthorityEnum())
                                                .cardDtoList(cardDtoList).build();
        return userInfoDto;
    }

    //앞면 카드 정보 수정
    // 변경할 수 있는 값 : profile, userName, hashTags
    public FrontResponseCardDto modifyFrontCard(FrontRequestCardDto frontRequestCardDto, User requestUser){

        if (requestUser == null) {
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.
        }

        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if (user.getEmail()!=null && !user.isWritedBy(requestUser))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.

        //tags 만들기
        List<String> stackTagStringList = frontRequestCardDto.getStackHashTags();
        List<String> interestTagStringList = frontRequestCardDto.getInterestHashTags();

        Map<List<HashTag>, String> userTagsList2String = makeTags(stackTagStringList, interestTagStringList);
        List<HashTag> tagList = userTagsList2String.keySet().iterator().next();
        String tagString = userTagsList2String.get(tagList);

        //user 값 넣기
        user.setProfile(frontRequestCardDto.getProfile());
        user.setUserName(frontRequestCardDto.getUserName());
        user.setTags(tagList);
        user.setHashTagsString(tagString);
        user = userRepository.save(user);

        //response
        FrontResponseCardDto frontResponseCardDto = FrontResponseCardDto.builder()
                                                    .profile(user.getProfile())
                                                    .userName(user.getUserName())
                                                    .hashTagsString(tagString).build();
        return frontResponseCardDto;
    }

    @Transactional
    public BackResponseCardDto createBackCard(BackRequestCardDto backRequestCardDto, User requestUser) {

        if(requestUser == null)
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.

        Card card = new Card();
        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if (user.getEmail()==null && !user.isWritedBy(requestUser))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.

        //tags 만들기
        List<String> stackTagStringList = backRequestCardDto.getStackHashTags();
        List<String> interestTagStringList = backRequestCardDto.getInterestHashTags();

        Map<List<HashTag>, String> userTagsList2String = makeTags(stackTagStringList, interestTagStringList);

        List<HashTag>  tagList = null;
        String tagString = null;
        Iterator iterator = userTagsList2String.keySet().iterator();
        if(iterator.hasNext()) {
            tagList = (List<HashTag>)iterator.next();
            userTagsList2String.get(tagList);
        }

        // card 값 넣기
        if(user.getCards().size() == 0) {
            user.setAuthorityEnum(AuthorityEnum.CAN_OTHER_READ);
            card.setCardOrder(1L);
        }
        else{
            card.setCardOrder(Long.valueOf(user.getCards().size()));
        }

        card.setUser(user);
        card.setTitle(backRequestCardDto.getTitle());
        card.setContent(backRequestCardDto.getContent());
        card.setHashTagsString(tagString);
        for(HashTag hashTag: tagList) {
            card.addHashTag(hashTag);
        }
//        card.setTags(tagList);
        cardRepository.save(card);


        //response
        BackResponseCardDto reponseBackCardDto = BackResponseCardDto.builder()
                                            .cardId(card.getId())
                                            .userId(user.getId())
                                            .title(card.getTitle())
                                            .content(card.getContent())
                                            .hashTagsString(card.getHashTagsString()).build();
        return reponseBackCardDto;
    }

    public BackResponseCardDto modifyBackCard(Long cardId, BackRequestCardDto backRequestCardDto, User requestUser) {

        if(requestUser == null)
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.

        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if (user.getEmail()!=null && !user.isWritedBy(requestUser))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.

        Card card = cardRepository.findById(cardId).orElse(null);
        if(card == null)
            throw new ContapException(ErrorCode.NOT_FOUND_CARD); //해당 카드를 찾을 수 없습니다.
        if(!card.isWritedBy(user))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.

        //tags 만들기
        List<String> stackTagStringList = backRequestCardDto.getStackHashTags();
        List<String> interestTagStringList = backRequestCardDto.getInterestHashTags();

        Map<List<HashTag>, String> userTagsList2String = makeTags(stackTagStringList, interestTagStringList);
        List<HashTag> tagList = userTagsList2String.keySet().iterator().next();
        String tagString = userTagsList2String.get(tagList);

        //card 값 넣기
        card.update(backRequestCardDto, tagList, tagString);
        cardRepository.save(card);

        //response
        return BackResponseCardDto.builder()
                .cardId(card.getId())
                .userId(card.getUser().getId())
                .title(backRequestCardDto.getTitle())
                .content(backRequestCardDto.getContent())
                .hashTagsString(tagString)
                .build();
    }

    public BackResponseCardDto deleteBackCard(Long cardId, User requestUser) {
        if(requestUser == null)
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.

        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if (user.getEmail()!=null && !user.isWritedBy(requestUser))
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //권한이 없습니다.

        Card card = cardRepository.findById(cardId).orElse(null);
        if(card == null)
            throw new ContapException(ErrorCode.NOT_FOUND_CARD); //해당 카드를 찾을 수 없습니다.
        if(!card.isWritedBy(user))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.


        cardRepository.delete(card);
        if(user.getCards().size()<1)
            user.setAuthorityEnum(AuthorityEnum.CANT_OTHER_READ);

        userRepository.save(user);

        //response
        return BackResponseCardDto.builder()
                .cardId(card.getId())
                .userId(card.getUser().getId())
                .title(card.getTitle())
                .content(card.getContent())
                .hashTagsString(card.getHashTagsString())
                .build();
    }

    private Map<List<HashTag>, String> makeTags(List<String> stacks, List<String> interests) {

        List<HashTag> hashTagList = new ArrayList<>();
        StringBuilder hashTagStr = new StringBuilder();

        //stackTags
        if(stacks.size()>0){
            hashTagStr.append("@");
            for(String str: stacks) {
                hashTagList.add(new HashTag(str, 0));
                hashTagStr.append(str+"@");
            }
        }

        //interestTags
        if(interests.size()>0){
            hashTagStr.append("_@");
            for(String str: interests) {
                hashTagList.add(new HashTag(str, 1));
                hashTagStr.append(str+"@");
            }
        }

        Map<List<HashTag>,String> tags = new HashMap<>();
        tags.put(hashTagList, hashTagStr.toString());
        return tags;
    }
}
