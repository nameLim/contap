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
import com.project.contap.util.MD5Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    public FrontResponseCardDto modifyFrontCard(MultipartFile files,FrontRequestCardDto frontRequestCardDto, User requestUser){

        if (requestUser == null) {
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.
        }
        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if (user.getEmail()!=null && !user.isWritedBy(requestUser))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.
        String filename = "basic.jpg";
        StringBuilder StacktagBuilder = new StringBuilder();
        String splitstr = "@_";
        StringBuilder interesttagBuilder = new StringBuilder();
        List<HashTag> hashTagList = frontRequestCardDto.getHashTags();

        if(hashTagList.size()>0) {
            for(HashTag hash: hashTagList) {
                if(hash.getType()==1){
                    interesttagBuilder.append("@" + hash.getName());
                }
                else{
                    StacktagBuilder.append("@" + hash.getName());
                }
            }
            interesttagBuilder.append("@");
        }
        StacktagBuilder.append(splitstr);
        StacktagBuilder.append(interesttagBuilder);
        try
        {
            if (files != null) {
                String origFilename = files.getOriginalFilename();
                filename = new MD5Generator(origFilename).toString() + ".jpg";
                String savePath = "/home/ubuntu/images/";

                if (!new File(savePath).exists()) {
                    try {
                        new File(savePath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                String filePath = savePath +filename;
                files.transferTo(new File(filePath));
            }
        }
        catch (Exception e) {
            throw new ContapException(ErrorCode.FILESAVE_ERROR);
        }
        //user 값 넣기
        user.setProfile("http://52.79.248.107:8080/display/" +filename);
        user.setUserName(frontRequestCardDto.getUserName());
        user.setTags(frontRequestCardDto.getHashTags());
        user.setHashTagsString(StacktagBuilder.toString());
        user = userRepository.save(user);
        //response
        return FrontResponseCardDto.builder()
                .profile(user.getProfile())
                .userName(user.getUserName())
                .hashTagsString(user.getHashTagsString()).build();
    }

    @Transactional
    public BackResponseCardDto createBackCard(BackRequestCardDto backRequestCardDto, User requestUser) {

        if(requestUser == null)
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.

        Card card = new Card();

        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if (user.getEmail()==null && !user.isWritedBy(requestUser))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.

        int cardSize = user.getCards().size();
        if( cardSize >= 10 )
            throw new ContapException(ErrorCode.EXCESS_CARD_MAX); //카드 최대 가능 수를 초과하였습니다.

        StringBuilder StacktagBuilder = new StringBuilder();
        String splitstr = "@_";
        StringBuilder interesttagBuilder = new StringBuilder();
        List<HashTag> hashTagList = backRequestCardDto.getHashTags();

        if(hashTagList.size()>0) {
            for(HashTag hash: hashTagList) {
                if(hash.getType()==1){
                    interesttagBuilder.append("@" + hash.getName());
                }
                else{
                    StacktagBuilder.append("@" + hash.getName());
                }
            }
            interesttagBuilder.append("@");
        }
        StacktagBuilder.append(splitstr);
        StacktagBuilder.append(interesttagBuilder);


        // card 값 넣기
        if(cardSize == 0) {
            user.setAuthorityEnum(AuthorityEnum.CAN_OTHER_READ);
            card.setCardOrder(1L);
        }
        else{
            card.setCardOrder(Long.valueOf(cardSize +1));
        }

        card.setUser(user);
        card.setTitle(backRequestCardDto.getTitle());
        card.setContent(backRequestCardDto.getContent());
        card.setTags(backRequestCardDto.getHashTags());
        card.setHashTagsString(StacktagBuilder.toString());
        cardRepository.save(card);

        //response
        return BackResponseCardDto.builder()
                .cardId(card.getId())
                .userId(user.getId())
                .title(card.getTitle())
                .content(card.getContent())
                .hashTagsString(card.getHashTagsString()).build();
    }

    @Transactional
    public BackResponseCardDto modifyBackCard(Long cardId, BackRequestCardDto backRequestCardDto, User requestUser) {

        if(requestUser == null)
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.

        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if (user.getEmail()!=null && !user.isWritedBy(requestUser))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.

        Card card = cardRepository.findById(cardId).orElse(null);
        if(card == null)
            throw new ContapException(ErrorCode.NOT_FOUND_CARD); //해당 카드를 찾을 수 없습니다.


        StringBuilder StacktagBuilder = new StringBuilder();
        String splitstr = "@_";
        StringBuilder interesttagBuilder = new StringBuilder();
        List<HashTag> hashTagList = backRequestCardDto.getHashTags();

        if(hashTagList.size()>0) {
            for(HashTag hash: hashTagList) {
                if(hash.getType()==1){
                    interesttagBuilder.append("@" + hash.getName());
                }
                else{
                    StacktagBuilder.append("@" + hash.getName());
                }
            }
            interesttagBuilder.append("@");
        }
        StacktagBuilder.append(splitstr);
        StacktagBuilder.append(interesttagBuilder);
        //card 값 넣기
        card.update(backRequestCardDto, StacktagBuilder.toString());
        card = cardRepository.save(card);

        //response
        return BackResponseCardDto.builder()
                .cardId(card.getId())
                .userId(user.getId())
                .title(card.getTitle())
                .content(card.getContent())
                .hashTagsString(card.getHashTagsString())
                .build();
    }

    @Transactional
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
        if(user.getCards().size()==1) {
            user.setAuthorityEnum(AuthorityEnum.CANT_OTHER_READ);
        }


        //response
        return BackResponseCardDto.builder()
                .cardId(card.getId())
                .userId(user.getId())
                .title(card.getTitle())
                .content(card.getContent())
                .hashTagsString(card.getHashTagsString())
                .build();
    }
}
