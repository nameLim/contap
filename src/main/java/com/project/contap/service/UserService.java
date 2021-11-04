package com.project.contap.service;

import com.project.contap.chatcontroller.ChatRoomRepository;
import com.project.contap.dto.*;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.HashTag;
import com.project.contap.model.QUser;
import com.project.contap.model.User;
import com.project.contap.repository.CardRepository;
import com.project.contap.repository.HashTagRepositoty;
import com.project.contap.repository.UserRepository;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.util.GetRandom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JPAQueryFactory jpaQueryFactory; // 이건차후에 쓸수도있을것같아서 남겨둠
    private final ChatRoomRepository chatRoomRepository;

    private static final String ADMIN_TOKEN = "AAABnv/xRVklrnYxKZ0aHgTBcXukeZygoC";

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,JPAQueryFactory jpaQueryFactory,ChatRoomRepository chatRoomRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jpaQueryFactory = jpaQueryFactory;
        this.chatRoomRepository =  chatRoomRepository;
    }

//    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JPAQueryFactory jpaQueryFactory, CardRepository cardRepository, HashTagRepositoty hashTagRepositoty) {
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//        this.jpaQueryFactory = jpaQueryFactory;
//        this.cardRepository = cardRepository;
//        this.hashTagRepositoty = hashTagRepositoty;
//    }


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
        if (!isValidEmail(email)) {
            throw new ContapException(ErrorCode.EMAIL_FORM_INVALID);
        }

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

    //이메일 검사
    private boolean isValidEmail(String email) {
        boolean err = false;
        String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(email);
        if (m.matches()) {
            err = true;
        }
        return err;
    }

    //로그인
    public User login(UserRequestDto requestDto) throws ContapException {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new ContapException(ErrorCode.USER_NOT_FOUND)
        );

        if (!passwordEncoder.matches(requestDto.getPw(), user.getPw())) {
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
        }

        return user;
    }


    //유저 프로필 사진 수정
    public User updateUserProfileImage(String profile, String userId) throws ContapException {
        User user = userRepository.findByEmail(userId).orElseThrow(
                () -> new ContapException(ErrorCode.USER_NOT_FOUND)
        );
        user.setProfile(profile);
        return userRepository.save(user);
    }


    //회원탈퇴
    @Transactional
    public void deleteUser(PwRequestDto requestDto, User user) throws ContapException {
        if (passwordEncoder.matches(requestDto.getPw(), user.getPw())) {
            userRepository.delete(user);
        }
        else {
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
        }
    }


    //비밀번호 변경
    private User getUsers(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ContapException(ErrorCode.REGISTER_ERROR));
    }

    @Transactional
    public void updatePassword(PwUpdateRequestDto requestDto, String email) throws ContapException {
        if (requestDto.getCurrentPw().isEmpty()) {
            throw new ContapException(ErrorCode.CURRENT_EMPTY_PASSWORD);
        }

        if (requestDto.getNewPw().isEmpty() || requestDto.getNewPw().isEmpty()) {
            throw new ContapException(ErrorCode.CHANGE_EMPTY_PASSWORD);
        }

        if (requestDto.getCurrentPw().equals(requestDto.getNewPw())) {
            throw new ContapException(ErrorCode.EQUAL_PREV_PASSWORD);
        }

        if (requestDto.getNewPw().length() < 6 || requestDto.getNewPw().length() > 20) {
            throw new ContapException(ErrorCode.PASSWORD_PATTERN_LENGTH);
        }

        if (!requestDto.getNewPw().equals(requestDto.getNewPwCheck())) {
            throw new ContapException(ErrorCode.NEW_PASSWORD_NOT_EQUAL);
        }


        User user = getUsers(email);

        if(!passwordEncoder.matches(requestDto.getCurrentPw(), user.getPw())){
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
        }
        if (!requestDto.getNewPw().equals(requestDto.getNewPwCheck())) {
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
        }
        String newPw = passwordEncoder.encode(requestDto.getNewPw());
        requestDto.setNewPw(newPw);

        user.updatePw(requestDto);
    }

    public String getAlarm(String email) {
        Boolean bAlarm = chatRoomRepository.readAlarm(email);
        return bAlarm.toString();
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