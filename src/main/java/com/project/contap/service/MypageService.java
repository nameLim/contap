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

    private final String SPLIT_CHAR = ",";

    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final HashTagRepositoty hashTagRepositoty;
    private final ImageService imageService;
    private final UserService userService;

    // 회원 정보 가져오기
    // 가져오는 값 : 기본 회원정보(앞면카드), 모든 뒷면카드
    public UserInfoDto getMyInfo(User requestUser) {

        User user = userService.checkUserAuthority(requestUser);
        List<Card> userCards = user.getCards();
        List<BackResponseCardDto> cardDtoList = new ArrayList<>();

        for(Card card: userCards) {
            BackResponseCardDto cardDto = makeBackResponseCardDto(card, user);
            cardDtoList.add(cardDto);
        }

        return UserInfoDto.builder()
                .userId(user.getId())
                .password(user.getPw())
                .userName(user.getUserName())
                .profile(user.getProfile())
                .field(user.getField())
                .authStatus(user.getAuthStatus())
                .cardDtoList(cardDtoList).build();
    }

    //앞카드 수정
    @Transactional
    public FrontResponseCardDto modifyFrontCard(FrontRequestCardDto frontRequestCardDto, User requestUser) throws IOException {
        User user = userService.checkUserAuthority(requestUser);

        // nickname 변경했을 경우 중복체크
        if(!user.getUserName().equals(frontRequestCardDto.getUserName())) {
            Optional<User> found = userRepository.existUserByUserName(frontRequestCardDto.getUserName());
            if (found.isPresent())
                throw new ContapException(ErrorCode.NICKNAME_DUPLICATE);
        }

        // profile 변경외에는 null
        String uploadImageUrl = "";
        if(frontRequestCardDto.getProfile()!=null){
            // profile 업로드
            uploadImageUrl = ImageService.upload(imageService, frontRequestCardDto.getProfile(), "static", user.getProfile());
        }
        else{
            // null이기 때문에 사용자의 기존 profile 가져오기
            uploadImageUrl = user.getProfile();
        }

        String requestTagStr = frontRequestCardDto.getHashTagsStr();
        Set<String> sets = new HashSet<>();

        if(requestTagStr.contains(SPLIT_CHAR)) {
            Collections.addAll(sets, requestTagStr.split(SPLIT_CHAR));
        }else if(requestTagStr.length()>0) {
            sets.add(requestTagStr);
        }

        List<HashTag> hashTagList = hashTagRepositoty.findAllByNameIn(sets);

        // HashTag
        StringBuilder retTagStrBuilder = new StringBuilder();
        StringBuilder intrestTagStrBuilder = new StringBuilder();
        for(HashTag tag: hashTagList) {
            if(tag.getType() == 0) { // stack
                retTagStrBuilder.append("@"+tag.getName());
            }
            else if(tag.getType() == 1) { // interest
                intrestTagStrBuilder.append(tag.getName()+"@");
            }
        }
        retTagStrBuilder.append("@_@");
        retTagStrBuilder.append(intrestTagStrBuilder);
        //user
        user.setProfile(uploadImageUrl);
        user.setUserName(frontRequestCardDto.getUserName());
        user.setTags(hashTagList);
        user.setHashTagsString(retTagStrBuilder.toString());
        user.setField(frontRequestCardDto.getField());
        user = userRepository.save(user);

        //response
        return FrontResponseCardDto.builder()
                .profile(user.getProfile())
                .userName(user.getUserName())
                .hashTagsString(user.getHashTagsString())
                .field(user.getField()).build();
    }

    //뒷카드 추가
    @Transactional
    public BackResponseCardDto createBackCard(BackRequestCardDto backRequestCardDto, User requestUser) {

        User user = userService.checkUserAuthority(requestUser);

        int cardSize = user.getCards().size();
        if( cardSize >= 10 )
            throw new ContapException(ErrorCode.EXCESS_CARD_MAX); //카드 최대 가능 수를 초과하였습니다.

        //card
        Card card = new Card();
        card.setCardOrder(Long.valueOf(cardSize +1));
        card.setUser(user);
        card.setTitle(backRequestCardDto.getTitle());
        card.setContent(backRequestCardDto.getContent());
        card.setTagsString(backRequestCardDto.getTagsStr());
        card.setLink(backRequestCardDto.getLink());
        card = cardRepository.save(card);

        int authStatus = user.getAuthStatus();
        authStatus = authStatus|AuthorityEnum.CAN_OTHER_READ.getAuthority();
        user.setAuthStatus(authStatus);

        //response
        return makeBackResponseCardDto(card,user);
    }

    //뒷카드 수정
    @Transactional
    public BackResponseCardDto modifyBackCard(Long cardId, BackRequestCardDto backRequestCardDto, User requestUser) {

        User user = userService.checkUserAuthority(requestUser);
        Card card = cardRepository.findById(cardId).orElse(null);
        if(card == null)
            throw new ContapException(ErrorCode.NOT_FOUND_CARD); //해당 카드를 찾을 수 없습니다.

        //card 값 넣기
        card.update(backRequestCardDto);
        card = cardRepository.save(card);

        //response
        return makeBackResponseCardDto(card,user);
    }

    //뒷카드 삭제
    @Transactional
    public BackResponseCardDto deleteBackCard(Long cardId, User requestUser) {

        User user = userService.checkUserAuthority(requestUser);
        Card card = cardRepository.findById(cardId).orElse(null);
        if(card == null)
            throw new ContapException(ErrorCode.NOT_FOUND_CARD); //해당 카드를 찾을 수 없습니다.
        if(!card.isWritedBy(user))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.

        cardRepository.delete(card);
        if(user.getCards().size()==1) {
            int authStatus = user.getAuthStatus();
            authStatus = authStatus-AuthorityEnum.CAN_OTHER_READ.getAuthority();
            user.setAuthStatus(authStatus);
        }

        //response
        return makeBackResponseCardDto(card,user);
    }

    // 뒷카드 Response make
    private BackResponseCardDto makeBackResponseCardDto(Card card, User user){
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
