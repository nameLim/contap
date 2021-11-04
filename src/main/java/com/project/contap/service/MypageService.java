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
import com.project.contap.util.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class MypageService {

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final HashTagRepositoty hashTagRepositoty;
    private final ImageService imageService;

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

    public FrontResponseCardDto modifyFrontCard(FrontRequestCardDto frontRequestCardDto, User requestUser) throws IOException {
    //userName, hashtagName
        if (requestUser == null) {
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.
        }
        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if (user.getEmail()!=null && !user.isWritedBy(requestUser))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.

        //가입 nickname 중복체크
        if(!user.getUserName().equals(frontRequestCardDto.getUserName())) {
            Optional<User> found = userRepository.findByUserName(frontRequestCardDto.getUserName());
            if (found.isPresent())
                throw new ContapException(ErrorCode.NICKNAME_DUPLICATE);
        }

        String uploadImageUrl = ImageService.upload(imageService, frontRequestCardDto.getProfile(), "static", user.getProfile());

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
        Collections.addAll(tagsSet, tagArr);

        List<HashTag> hashTagList = hashTagRepositoty.findAllByNameIn(tagsSet);

        //user 값 넣기
        user.setProfile(uploadImageUrl);
        user.setUserName(frontRequestCardDto.getUserName());
        user.setTags(hashTagList);
        user.setHashTagsString(frontRequestCardDto.getHashTagsStr());
        user.setField(frontRequestCardDto.getField());
        user = userRepository.save(user);

        //response
        return FrontResponseCardDto.builder()
                .profile(user.getProfile())
                .userName(user.getUserName())
                .hashTagsString(user.getHashTagsString())
                .field(user.getField()).build();
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
        card.setLink(backRequestCardDto.getLink());
        card = cardRepository.save(card);

        //response
        return BackResponseCardDto.builder()
                .cardId(card.getId())
                .userId(user.getId())
                .title(card.getTitle())
                .content(card.getContent())
                .tagsStr(card.getTagsString())
                .link(card.getLink())
                .field(user.getField()).build();
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
                .field(user.getField())
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
                .field(user.getField())
                .build();
    }
}
