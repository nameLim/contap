package com.project.contap.service;

import com.project.contap.dto.*;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.AuthorityEnum;
import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.model.User;
import com.project.contap.repository.CardRepository;
import com.project.contap.repository.HashTagRepositoty;
import com.project.contap.repository.UserRepository;
import com.project.contap.util.MD5Generator;
import lombok.RequiredArgsConstructor;
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
    private final HashTagRepositoty hashTagRepositoty;

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
                    .content(card.getContent())
                    .tagsStr(card.getTagsString())
                    .link(card.getLink())
                    .build();
            cardDtoList.add(cardDto);
        }

        return UserInfoDto.builder()
                .userId(user.getId())
                .password(user.getPw())
                .userName(user.getUserName())
                .profile(user.getProfile())
                .authorityEnum(user.getAuthorityEnum())
                .cardDtoList(cardDtoList).build();
    }

    public FrontResponseCardDto modifyFrontCard(FrontRequestCardDto frontRequestCardDto, User requestUser){
    //userName, hashtagName
        if (requestUser == null) {
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.
        }
        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if (user.getEmail()!=null && !user.isWritedBy(requestUser))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.

        String filename = "basic.jpg";
        MultipartFile files = frontRequestCardDto.getProfile();
        String savePath;
        String filePath="";
        try
        {
            if (files != null) {
                String origFilename = files.getOriginalFilename();
                filename = new MD5Generator(origFilename).toString() + ".jpg";
                savePath = "/home/ubuntu/contap/image/";

                if (!new File(savePath).exists()) {
                    try {
                        new File(savePath).mkdir();
                    } catch (Exception e) {
                        e.getStackTrace();
                    }
                }
                filePath = savePath +filename;
                System.out.println(filePath);
                files.transferTo(new File(filePath));
            }
        }
        catch (Exception e) {
            throw new ContapException(ErrorCode.FILESAVE_ERROR);
        }

        String requestTagStr = frontRequestCardDto.getHashTagsStr();
        List<String> tagsList = new ArrayList<>();
        String[] tagArr = new String[0];

        if(requestTagStr.contains("@_@")) {
            String[] tagsArr = requestTagStr.split("@_@");
            tagsList.addAll(Arrays.asList(tagsArr[0].split("@")));
            tagsList.addAll(Arrays.asList(tagsArr[1].split("@")));
        }
        else if(requestTagStr.contains("@")) {
            tagArr = requestTagStr.split("@");
        }
        Set<String> tagsSet = new HashSet<>();
        for(String str: tagArr) {
            tagsSet.add(str);
        }

        List<HashTag> hashTagList = hashTagRepositoty.findAllByNameIn(tagsSet);

        //user 값 넣기
        user.setProfile("http://52.79.199.239" + filePath);
        user.setUserName(frontRequestCardDto.getUserName());
        user.setTags(hashTagList);
        user.setHashTagsString(frontRequestCardDto.getHashTagsStr());
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
        card.setTagsString(backRequestCardDto.getTagsStr());
        card = cardRepository.save(card);

        //response
        return BackResponseCardDto.builder()
                .cardId(card.getId())
                .userId(user.getId())
                .title(backRequestCardDto.getTitle())
                .content(backRequestCardDto.getContent())
                .tagsStr(backRequestCardDto.getTagsStr())
                .link(backRequestCardDto.getLink()).build();
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

        //card 값 넣기
        card.update(backRequestCardDto);
        card = cardRepository.save(card);

        //response
        return BackResponseCardDto.builder()
                .cardId(card.getId())
                .userId(user.getId())
                .title(card.getTitle())
                .content(card.getContent())
                .tagsStr(card.getTagsString())
                .link(card.getLink())
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
                .tagsStr(card.getTagsString())
                .link(card.getLink())
                .build();
    }
}
