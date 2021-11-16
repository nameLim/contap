package com.project.contap.service;

import com.project.contap.chat.ChatRoomRepository;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.user.User;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.PwUpdateRequestDto;
import com.project.contap.model.user.dto.SignUpRequestDto;
import com.project.contap.model.user.dto.UserLoginDto;
import com.project.contap.security.jwt.JwtTokenProvider;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceTest {


    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    UserRepository userRepository;

    @Mock
    ChatRoomRepository chatRoomRepository;


    @Nested
    @DisplayName("회원가입 테스트")
    class registerUser {

        @Test
        @DisplayName("회원가입 - 정상 케이스")
        public void registerUserTest_Normal() {

            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234qwer", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            User user2 = User.builder()
                    .email(requestDto.getEmail())
                    .pw("mytest")
                    .userName(requestDto.getUserName())
                    .build();

            when(passwordEncoder.encode(requestDto.getPw()))
                    .thenReturn("mytest");
            when(userRepository.save(user2))
                    .thenReturn(user2);

            User user = userService.registerUser(requestDto);

            assertEquals(user.getEmail(), requestDto.getEmail());
            assertEquals(user.getPw(), "mytest");
            assertEquals(user.getUserName(), requestDto.getUserName());
        }

        @Test
        @DisplayName("회원가입 - 실패 케이스 - 이메일안쓴경우")
        public void register_emptyemail() {

            SignUpRequestDto requestDto = new SignUpRequestDto("", "1234qwer", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.REGISTER_ERROR);

        }

        @Test
        @DisplayName("회원가입 - 실패 케이스 - pw안쓴경우")
        public void register_emptypw() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.REGISTER_ERROR);

        }

        @Test
        @DisplayName("회원가입 - 실패 케이스 - userName안쓴경우")
        public void register_emptyuserName() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234qwer", "1234qwer", "");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.REGISTER_ERROR);

        }

        @Test
        @DisplayName("회원가입 - 실패케이스 - 이메일형식이 맞지 않은경우")
        public void register_emailValid() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test", "1234qwer", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_FORM_INVALID);
        }

        @Test
        @DisplayName("회원가입 - 실패케이스 - 비밀번호 6자리 미만인경우")
        public void register_pwValid() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234", "1234", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.PASSWORD_PATTERN_LENGTH);
        }

        @Test
        @DisplayName("회원가입 - 실패케이스 - 비밀번호랑 체크가 일치하지 않은 경우")
        public void register_pw_notEqual() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234qwer", "qwer1234", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.NOT_EQUAL_PASSWORD);

        }


        @Test
        @DisplayName("회원가입 - 실패케이스 - 이메일이 중복되었을 경우")
        public void register_emailDuplicate() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234qwer", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            User user = User.builder()
                    .email("test@naver.com")
                    .pw("1234qwer")
                    .userName("test1")
                    .build();

            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.EMAIL_DUPLICATE);
        }

        @Test
        @DisplayName("회원가입 - 실패케이스 - username이 중복되었을 경우")
        public void register_userNameDuplicate() {
            SignUpRequestDto requestDto = new SignUpRequestDto("test@naver.com", "1234qwer", "1234qwer", "test");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            User user = User.builder()
                    .email("test1@naver.com")
                    .pw("1234qwer")
                    .userName("test")
                    .build();

            when(userRepository.findByUserName(user.getUserName()))
                    .thenReturn(Optional.of(user));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.registerUser(requestDto);
            });

            assertEquals(exception.getErrorCode(), ErrorCode.NICKNAME_DUPLICATE);

        }


    }

    @Nested
    @DisplayName("로그인 테스트")
    class login {
        @Test
        @DisplayName("로그인 - 정상케이스")
        public void login_user() {

            UserLoginDto loginDto = new UserLoginDto("test@naver.com", "1234qwer");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            User user = User.builder()
                    .email("test@naver.com")
                    .pw("1234qwer")
                    .userName("test")
                    .build();

            when(passwordEncoder.matches(loginDto.getPw(), user.getPw()))
                    .thenReturn(true);
            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));

            User user1 = userService.login(loginDto);


            assertEquals(user1.getEmail(), user.getEmail());
            assertEquals(user1.getPw(), user.getPw());
            assertEquals(user1.getUserName(), user.getUserName());


        }

//        @Test
//        @DisplayName("로그인 - 실패케이스 - 이메일이 틀릴 경우")
//        public void login_useremail() {
//
//            UserLoginDto loginDto = new UserLoginDto("test1@naver.com","1234qwer");
//            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);
//
//            User user = User.builder()
//                    .email("test@naver.com")
//                    .pw("1234qwer")
//                    .userName("test")
//                    .build();
//
//            when(passwordEncoder.matches(loginDto.getPw(),user.getPw()))
//                    .thenReturn(true);
//            when(userRepository.findByEmail(user.getEmail()))
//                    .thenReturn(null);
//
//            User user1 = userService.login(loginDto);
//
//            ContapException exception = assertThrows(ContapException.class, () -> {
//                userService.login(loginDto);
//            });
//
//            assertEquals(exception.getErrorCode(),ErrorCode.USER_NOT_FOUND);
//
//
//
//        }

        @Test
        @DisplayName("로그인 - 실패케이스 - 비밀번호가 틀릴 경우")
        public void login_pw() {
            UserLoginDto loginDto = new UserLoginDto("test@naver.com", "123456");
            UserService userService = new UserService(passwordEncoder, userRepository, chatRoomRepository);

            User user = User.builder()
                    .email("test@naver.com")
                    .pw("1234qwer")
                    .userName("test")
                    .build();

            when(passwordEncoder.matches(loginDto.getPw(), user.getPw()))
                    .thenReturn(false);
            when(userRepository.findByEmail(user.getEmail()))
                    .thenReturn(Optional.of(user));


            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.login(loginDto);
            });


            assertEquals(exception.getErrorCode(), ErrorCode.NOT_EQUAL_PASSWORD);

        }
    }

    @Nested
    @DisplayName("비밀번호 변경 테스트")
    class changePw {

        @Test
        @DisplayName("비밀번호 변경 - 실패케이스 - 현재 비번 빈값일때")
        public void currentPw_empty() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("","qwer1234","qwer1234");
            String email = "test@naver.com";

            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.updatePassword(requestDto,email);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.CURRENT_EMPTY_PASSWORD);

        }

        @Test
        @DisplayName("비밀번호 변경 - 실패케이스 - 새로운비번 빈값일때")
        public void newPw_empty() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("1234qwer","","qwer1234");
            String email = "test@naver.com";

            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.updatePassword(requestDto,email);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.CHANGE_EMPTY_PASSWORD);

        }

        @Test
        @DisplayName("비밀번호 변경 - 실패케이스 - 현재비번 새로운비번 같을때")
        public void newPw_currentPw_equal() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("1234qwer","1234qwer","1234qwer");
            String email = "test@naver.com";

            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.updatePassword(requestDto,email);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.EQUAL_PREV_PASSWORD);

        }

        @Test
        @DisplayName("비밀번호 변경 - 실패케이스 - 새로운비번 6자리 미만일때")
        public void newPw_length() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("1234qwer","1234","1234");
            String email = "test@naver.com";

            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.updatePassword(requestDto,email);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.PASSWORD_PATTERN_LENGTH);

        }

        @Test
        @DisplayName("비밀번호 변경 - 실패케이스 - 새로운비번과 체크가 같지 않을 때")
        public void newPw_newPwCheck_equal() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("1234qwer","qwer1234","asdf1234");
            String email = "test@naver.com";

            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(userRepository.findByEmail(email)).thenReturn(Optional.of(new User()));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.updatePassword(requestDto,email);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.NEW_PASSWORD_NOT_EQUAL);

        }

        @Test
        @DisplayName("비밀번호 변경 - 실패케이스 - 현재비밀번호입력과 기존비밀번호가 일치하지 않을때")
        public void pw_currentPw_equal() {
            PwUpdateRequestDto requestDto = new PwUpdateRequestDto("1234qwer","qwer1234","qwer1234");
            String email = "test@naver.com";
            User user = User.builder()
                    .email("test@naver.com")
                    .pw("zxcvasdf")
                    .userName("test")
                    .build();


            UserService userService = new UserService(passwordEncoder,userRepository,chatRoomRepository);

            when(passwordEncoder.matches(requestDto.getCurrentPw(), user.getPw()))
                    .thenReturn(false);
            when(userRepository.findByEmail(email))
                    .thenReturn(Optional.of(user));

            ContapException exception = assertThrows(ContapException.class, () -> {
                userService.updatePassword(requestDto,email);
            });

            assertEquals(exception.getErrorCode(),ErrorCode.NOT_EQUAL_PASSWORD);

        }




    }

}





