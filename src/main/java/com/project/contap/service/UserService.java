package com.project.contap.service;

import com.project.contap.dto.*;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
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
    private final JPAQueryFactory jpaQueryFactory; // 이건차후에 쓸수도있을것같아서 남겨둠


    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,JPAQueryFactory jpaQueryFactory) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jpaQueryFactory =jpaQueryFactory;
    }


    public User registerUser(SignUpRequestDto requestDto) throws ContapException {
        if (requestDto.getEmail() == "") {
            throw new ContapException(ErrorCode.REGISTER_ERROR);
        }
        if (requestDto.getPw() == "") {
            throw new ContapException(ErrorCode.REGISTER_ERROR);
        }
        if (requestDto.getPwCheck() == "") {
            throw new ContapException(ErrorCode.REGISTER_ERROR);
        }
        if (requestDto.getUserName() == "") {
            throw new ContapException(ErrorCode.REGISTER_ERROR);
        }

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
                    throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
                }
            } else {
                throw new ContapException(ErrorCode.PASSWORD_PATTERN_LENGTH);

            }
        } else {
            throw new ContapException(ErrorCode.PASSWORD_ENTER);
        }

        // 패스워드 암호화
        String pw = passwordEncoder.encode(requestDto.getPw());

        //회원정보 저장
        User user = new User(email, pw, userName);
        return userRepository.save(user);
    }

    //로그인
    public User login(UserRequestDto requestDto) throws ContapException {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new ContapException(ErrorCode.USER_NOT_FOUND)
        );

        if (!passwordEncoder.matches(requestDto.getPw(), user.getPw())) {
            throw new ContapException(ErrorCode.USER_NOT_FOUND);
        }

        return user;
    }

    // 가입 중복 email
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

    //로그인 닉네임 중복체크
    public Map<String, String> duplicateuserName(SignUpRequestDto signUpRequestDto) {
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
    //유저 프로필 사진 수정
    public User updateUserProfileImage(String profile, String userId) throws ContapException {
        User user = userRepository.findByEmail(userId).orElseThrow(
                () -> new ContapException(ErrorCode.USER_NOT_FOUND)
        );
        user.setProfile(profile);
        return userRepository.save(user);
    }
    @Transactional
    public void deleteUser( PwRequestDto requestDto) throws ContapException {
        if (!requestDto.getPw().equals(requestDto.getPwCheck())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        User user = userRepository.findById(requestDto.getId()).orElseThrow(
                ()-> new ContapException(ErrorCode.NOT_EQUAL_PASSWORD)
        );
        if (passwordEncoder.matches(requestDto.getPw(), user.getPw())) {
            userRepository.delete(user);
        }

    }

//    private User getUsers(String email) throws ContapException {
//        return userRepository.findByEmail(email)
//                .orElseThrow(() -> new ContapException(ErrorCode.REGISTER_ERROR));
//    }

    //비밀번호 변경
    @Transactional
    public void updatePassword(PwUpdateRequestDto requestDto) throws ContapException {
        User user = userRepository.findById(requestDto.getId()).orElseThrow(
                ()->  new ContapException(ErrorCode.REGISTER_ERROR)
        );

        if(!passwordEncoder.matches(requestDto.getCurrentPw(), user.getPw())){
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
        }

        String newPw = passwordEncoder.encode(requestDto.getNewPw());
        requestDto.setNewPw(newPw);

        user.updatePw(requestDto);
    }
}

//    @Transactional //table join으로 검색.
//    public List<UserRequestDto> getuser(List<HashTag> hashTags) {
//        QUser hu = QUser.user;
//        List<Long> ids2 = Arrays.asList(new Long(GetRandom.randomRange(1,10)),new Long(GetRandom.randomRange(1,10)),new Long(GetRandom.randomRange(1,10)));
//        List<UserRequestDto> abc;
//        abc = jpaQueryFactory
//                .select(
//                        Projections.constructor(UserRequestDto.class,
//                                hu.id,
//                                hu.email,
//                                hu.profile,
//                                hu.kakaoId,
//                                hu.userName,
//                                hu.pw,
//                                hu.hashTagsString
//                        )).distinct()
//                .from(hu)
//                .where(hu.tags.any().id.in(ids2))
//                .offset(9).limit(9)
//                .fetch();
//        return abc;
//    }
//
//    @Transactional // table join 없이 검색
//    public List<UserRequestDto> getuser2(List<HashTag> hashTags,int type) {
//        BooleanBuilder builder = new BooleanBuilder();
//        QUser hu = QUser.user;
//        int a = GetRandom.randomRange(0,19);
//        int b = GetRandom.randomRange(0,19);
//        int c = GetRandom.randomRange(0,19);
//        List<String> ids2 = Arrays.asList(tagnames.get(a),tagnames.get(c),tagnames.get(b));
//        if (type == 0) {
//            for (String tagna : ids2) {
//                builder.or(hu.hashTagsString.contains(tagna));
//            }
//        }
//        else
//        {
//            for (String tagna : ids2) {
//                builder.and(hu.hashTagsString.contains(tagna));
//            }
//        }
//        List<UserRequestDto> abc;
//        abc = jpaQueryFactory
//                .select(
//                        Projections.constructor(UserRequestDto.class,
//                                hu.id,
//                                hu.email,
//                                hu.profile,
//                                hu.kakaoId,
//                                hu.userName,
//                                hu.pw,
//                                hu.hashTagsString
//                        )).distinct()
//                .from(hu)
//                .where(builder)
//                .offset(9).limit(9)
//                .fetch();
//        return abc;
//    }