package com.project.contap.service;

import com.project.contap.dto.SignUpRequestDto;
import com.project.contap.dto.UserRequestDto;
import com.project.contap.dto.UserResponseDto;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.Card;
import com.project.contap.model.User;
import com.project.contap.repository.CardRepository;
import com.project.contap.model.*;
import com.project.contap.repository.CardRepository;
import com.project.contap.repository.HashTagRepositoty;
import com.project.contap.repository.UserRepository;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.util.GetRandom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.*;

@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JPAQueryFactory jpaQueryFactory;
    private final CardRepository cardRepository;
    private final HashTagRepositoty hashTagRepositoty;


    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,JPAQueryFactory jpaQueryFactory,CardRepository cardRepository,HashTagRepositoty hashTagRepositoty) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jpaQueryFactory =jpaQueryFactory;
        this.cardRepository = cardRepository;
        this.hashTagRepositoty = hashTagRepositoty;
    }


    public User registerUser(SignUpRequestDto requestDto) throws ContapException {

        // 패스워드 암호화
        String pw = passwordEncoder.encode(requestDto.getPw());

        //가입 email(id) 중복체크
        String email = requestDto.getEmail();
        Optional<User> found = userRepository.findByEmail(email);
        if (found.isPresent()) {
            throw new ContapException(ErrorCode.EMAIL_DUPLICATE);
        }


        //가입 nickname 중복체크
        String userName = requestDto.getUserName();
        Optional<User> found2 = userRepository.findByUserName(userName);
        if (found2.isPresent()) {
            throw new ContapException(ErrorCode.NICKNAME_DUPLICATE);
        }

        //비밀번호확인
        String password = requestDto.getPw();
        String passwordCheck = requestDto.getPwCheck();

        if (!password.isEmpty() && !passwordCheck.isEmpty()) {
            if (password.length() >= 6 && password.length() <= 20) {
                if (!password.equals(passwordCheck)) {
                    throw new ContapException(ErrorCode.USER_NOT_FOUND);
                }
            } else {
                throw new ContapException(ErrorCode.PASSWORD_PATTERN_LENGTH);

            }
        } else {
            throw new ContapException(ErrorCode.PASSWORD_ENTER);
        }




        User user = new User(email, pw, userName);
        return userRepository.save(user);
    }

    public User login(UserRequestDto requestDto) throws ContapException {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new ContapException(ErrorCode.USER_NOT_FOUND)
        );

        // 패스워드 암호화
        if (!passwordEncoder.matches(requestDto.getPw(), user.getPw())) {
            throw new ContapException(ErrorCode.USER_NOT_FOUND);
        }

        return user;
    }

    // 로그인 중복 email
    public Map<String, String> duplicateId(UserRequestDto userRequestDto) {
        User user = userRepository.findByEmail(userRequestDto.getEmail()).orElse(null);

        Map<String, String> result = new HashMap<>();
        if (user == null) {
            result.put("result", "success");
            return result;
        }

        result.put("result", "fail");
        result.put("message", "중복된 email이 존재합니다.");
        return result;
    }

    //로그인 중복 닉네임
    public Map<String, String> duplicateNickname(SignUpRequestDto signUpRequestDto) {
        User user = userRepository.findByUserName(signUpRequestDto.getUserName()).orElse(null);
        Map<String, String> result = new HashMap<>();
        if (user == null) {
            result.put("result", "success");
            return result;
        }

        result.put("result", "fail");
        result.put("message", "중복된 닉네임이 있습니다.");
        return result;
    }
    // 유저 정보 뿌리기
    public Page<User> main(Pageable pageable) {
//        return userRepository.findAllByOrderByModifiedDtDesc(pageable);
        return null;
    }

    public User getUsers(Long id) throws ContapException {
        return userRepository.findById(id).orElseThrow(
                () -> new ContapException(ErrorCode.CARD_NOT_FOUND)
        );
    }

    public List<UserResponseDto> getUserDtoList(UserDetailsImpl userDetails) {
        List<User> users = userRepository.findAll();
        return UserResponseDto.listOf(users, userDetails);
    }

    public User updateUserProfileImage(String profile, String userId) throws ContapException {
        User user = userRepository.findByEmail(userId).orElseThrow(
                () -> new ContapException(ErrorCode.USER_NOT_FOUND)
        );
        user.setProfile(profile);
        return userRepository.save(user);
    }
    @Transactional
    public List<UserRequestDto> getuser(List<HashTag> hashTags) {
        QUser hu = QUser.user;
        List<Long> ids2 = Arrays.asList(new Long(GetRandom.randomRange(5001,5500)),new Long(GetRandom.randomRange(5001,5500)),new Long(GetRandom.randomRange(5001,5500)));
        List<UserRequestDto> abc;
        abc = jpaQueryFactory
                .select(
                        Projections.constructor(UserRequestDto.class,
                                hu.id,
                                hu.email,
                                hu.profile,
                                hu.kakaoId,
                                hu.userName,
                                hu.pw
                        )).distinct()
                .from(hu)
                .where(hu.tags.any().id.in(ids2))
                .offset(9).limit(9)
                .fetch();
        return abc;
    }
}

