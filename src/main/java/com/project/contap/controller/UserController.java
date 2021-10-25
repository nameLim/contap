package com.project.contap.controller;

import com.project.contap.dto.SignUpRequestDto;
import com.project.contap.dto.UserRequestDto;
import com.project.contap.exception.ContapException;
import com.project.contap.model.User;
import com.project.contap.security.jwt.JwtTokenProvider;
import com.project.contap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider) {
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    //가입 요청 처리
    @PostMapping("/user/signup")
    public Map<String, String> registerUser(@RequestBody SignUpRequestDto requestDto) throws ContapException {
        User user = userService.registerUser(requestDto);
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        result.put("id", String.valueOf(user.getId()));
        result.put("email", user.getEmail());
        result.put("userName", user.getUserName());

        return result;
    }


    // 로그인
    @PostMapping("/user/login")
    public Map<String, String> login(@RequestBody UserRequestDto requestDto) throws ContapException {
        User user = userService.login(requestDto);

        Map<String, String> result = new HashMap<>();
        result.put("token", jwtTokenProvider.createToken(user.getEmail(), user.getEmail(), user.getUserName())); // "username" : {username}
        result.put("email", user.getEmail());
        result.put("nickname", user.getUserName());
        result.put("result", "success");

        return result;
    }

    @PostMapping("/signup/emailcheck")
    public Map<String, String> duplicateId(@RequestBody UserRequestDto userRequestDto) {
        return userService.duplicateId(userRequestDto);
    }

    @PostMapping("/signup/duplicate_nickname")
    public Map<String, String> duplicateNickname(@RequestBody SignUpRequestDto signUpRequestDto) {
        return userService.duplicateNickname(signUpRequestDto);
    }


//    @GetMapping("/auth")
//    public Map<String, String> loginCheck(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ContapException {
//        if (userDetails == null) {
//            throw new ContapException(ErrorCode.LOGIN_TOKEN_EXPIRE);
//        }
//        Map<String, String> result = new HashMap<>();
//        result.put("email", userDetails.getUser().getEmail());
//        result.put("nickname", userDetails.getUser().getNickname());
//        result.put("result", "success");
//
//        return result;
//    }
}
