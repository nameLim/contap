package com.project.contap.service;

import com.project.contap.dto.*;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.model.QUser;
import com.project.contap.model.User;
import com.project.contap.repository.CardRepository;
import com.project.contap.repository.UserRepository;
import com.project.contap.util.GetRandom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MypageService {

    private final UserRepository userRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final CardRepository cardRepository;

    // 회원 정보 가져오기
    // 가져오는 값 : 기본 회원정보(앞면카드), 모든 뒷면카드
    public User getMyInfo(User requestUser) {
        User u = userRepository.findById(requestUser.getId()).orElse(null);
        if (u != null)
            return u;
        return null;
    }
    //앞면 카드 정보 수정
    // 여기서 연관관계가 잘 변경 되는지 확인해야한다 - 확인 완료
    public UserFrontCardDto modifyFrontCard(UserFrontCardDto userFrontCardDto, User requestUser){
        User u = userRepository.getById(requestUser.getId());
        u.setProfile(userFrontCardDto.getProfile());
        u.setUserName(userFrontCardDto.getUserName());
        u.setTags(userFrontCardDto.getStackHashTags());
        userRepository.save(u);
        return null;
    }

    public void addProject(ProjectAddDto projectAddDto, User user) {
        User u = userRepository.findById(user.getId()).orElse(null);
        Long a = new Long(u.getCards().size()+1);
        Card ncard = new Card(user,a,projectAddDto.getTitle(),projectAddDto.getContent(),projectAddDto.getStackHashTags());
        cardRepository.save(ncard);
    }

    public void editCard(Long cardId, ProjectAddDto projectAddDto, User user) {
        Card c = cardRepository.findById(cardId).orElse(null);
        c.setTitle(projectAddDto.getTitle());
        c.setContent(projectAddDto.getContent());
        c.setTags(projectAddDto.getStackHashTags());
        cardRepository.save(c);
    }

    public void delCard(Long cardId, User user) {
        Card c = cardRepository.findById(cardId).orElse(null);
        if(user.getId().equals(c.getUser().getId()))
        {
            cardRepository.delete(c);
        }
    }
}
