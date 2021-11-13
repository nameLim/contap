package com.project.contap.service;

import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.common.enumlist.AuthorityEnum;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.PwUpdateRequestDto;
import com.project.contap.model.user.dto.SignUpRequestDto;
import com.project.contap.model.user.dto.UserLoginDto;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JPAQueryFactory jpaQueryFactory; // 이건차후에 쓸수도있을것같아서 남겨둠
    private final ChatRoomRepository chatRoomRepository;

    public User registerUser(SignUpRequestDto requestDto) throws ContapException {

        checkValidation(requestDto);
        // 패스워드 암호화
        String pw = passwordEncoder.encode(requestDto.getPw());
        User.userCount = User.userCount+1;

        User user = User.builder()
                .email(requestDto.getEmail())
                .pw(pw)
                .userName(requestDto.getUserName())
                .build();
        return userRepository.save(user);
    }

    public User login(UserLoginDto requestDto) throws ContapException {
        User user = userRepository.findByEmail(requestDto.getEmail()).orElseThrow(
                () -> new ContapException(ErrorCode.USER_NOT_FOUND)
        );

        if (!passwordEncoder.matches(requestDto.getPw(), user.getPw())) {
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
        }

        return user;
    }

//    @Transactional
//    public void deleteUser(PwRequestDto requestDto, User user) throws ContapException {
//        if (passwordEncoder.matches(requestDto.getPw(), user.getPw())) {
//            userRepository.delete(user);
//            User.userCount = User.userCount-1;
//        }
//        else {
//            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
//        }
//    }

    @Transactional
    public void deleteUser(String password, User user) throws ContapException {
        if (passwordEncoder.matches(password, user.getPw())) {
            userRepository.delete(user);
            User.userCount = User.userCount-1;
        }
        else {
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
        }
    }

    @Transactional
    public void updatePassword(PwUpdateRequestDto requestDto, String email) throws ContapException {
        User user =userRepository.findByEmail(email)
                .orElseThrow(() -> new ContapException(ErrorCode.REGISTER_ERROR));
        checkUpdatePwVal(requestDto,user);
        String newPw = passwordEncoder.encode(requestDto.getNewPw());
        requestDto.setNewPw(newPw);
        user.updatePw(requestDto);
    }

    public String[] getAlarm(String email) {
        String[] alarms = chatRoomRepository.readAlarm(email);
        return alarms;
    }

    public String modifyPhoneNumber(String phoneNumber, User requestUser) {

        User user = checkUserAuthority(requestUser);

        //핸드폰번호 정규식 검사
        if(!isValidPhoneNumber(phoneNumber)) {
            throw new ContapException((ErrorCode.PHONE_FORM_INVALID)); //핸드폰번호 형식이 맞지 않습니다.
        }

        //휴대폰번호 중복 확인
        Boolean found = userRepository.existUserByPhoneNumber(phoneNumber);
        if (found) {
            throw new ContapException(ErrorCode.PHONE_DUPLICATE); //중복된 핸드폰번호가 존재합니다.
        }
        user.setPhoneNumber(phoneNumber.replace("-",""));
        userRepository.save(user);
        return phoneNumber.replaceFirst("^([0-9]{4})([0-9]{4})$", "$1-$2");
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        String regex = "^010-\\d{4}-\\d{4}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    public User checkUserAuthority(User requestUser) {
        if(requestUser == null)
            throw new ContapException(ErrorCode.USER_NOT_FOUND); //회원 정보를 찾을 수 없습니다.

        User user = userRepository.findById(requestUser.getId()).orElse(null);
        if (user.getEmail()!=null && !user.isWrittenBy(requestUser))
            throw new ContapException(ErrorCode.ACCESS_DENIED); //권한이 없습니다.

        return user;
    }

    public void changeAlarmState(int alarmState, User requestUser) {
        User user = checkUserAuthority(requestUser);
        int authStatus = user.getAuthStatus();
        if(alarmState==0) {
            authStatus = authStatus - AuthorityEnum.ALARM.getAuthority();
        }
        else if(alarmState==1) {
            authStatus = authStatus|AuthorityEnum.ALARM.getAuthority();
        }
        user.setAuthStatus(authStatus);
        userRepository.save(user);
    }

    private void checkValidation(SignUpRequestDto requestDto) {
        if (requestDto.getEmail().equals("")) {
            throw new ContapException(ErrorCode.REGISTER_ERROR);
        }
        if (requestDto.getPw().equals("")) {
            throw new ContapException(ErrorCode.REGISTER_ERROR);
        }
        if (requestDto.getPwCheck().equals("")) {
            throw new ContapException(ErrorCode.REGISTER_ERROR);
        }
        if (requestDto.getUserName().equals("")) {
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
    }

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

    private void checkUpdatePwVal(PwUpdateRequestDto requestDto, User user) {
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

        if(!passwordEncoder.matches(requestDto.getCurrentPw(), user.getPw())){
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
        }
        if (!requestDto.getNewPw().equals(requestDto.getNewPwCheck())) {
            throw new ContapException(ErrorCode.NOT_EQUAL_PASSWORD);
        }
    }
}

