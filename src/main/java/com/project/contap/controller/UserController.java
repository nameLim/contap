package com.project.contap.controller;

import com.project.contap.dto.PwRequestDto;
import com.project.contap.dto.PwUpdateRequestDto;
import com.project.contap.dto.SignUpRequestDto;
import com.project.contap.dto.UserRequestDto;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.User;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.security.jwt.JwtTokenProvider;
import com.project.contap.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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
        result.put("userName", user.getUserName());
        result.put("result", "success");
        result.put("alarm", userService.getAlarm(user.getEmail()));

        return result;
    }




//    @PostMapping("/user/image")
//    public Map<String, String> updateUserProfileImage(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
//            @RequestBody ProfileRequestDto requestDto) throws ContapException {
//        if (userDetails == null) {
//            throw new AuthenticationServiceException("로그인이 필요합니다.");
//        }
//        User user = userService.updateUserProfileImage(requestDto.getProfile(), userDetails.getUser().getEmail());
//        Map<String, String> result = new HashMap<>();
//
//        result.put("profile", user.getProfile());
//        result.put("email", user.getEmail());
//        result.put("result", "success");
//
//        return result;
//    }

    //회원탈퇴
    @DeleteMapping("/setting/withdrawal")
    public Map<String, String> deleteUser(@RequestBody PwRequestDto requestDto,@AuthenticationPrincipal UserDetailsImpl userDetails) throws ContapException {


        userService.deleteUser(requestDto,userDetails.getUser());

        Map<String, String> result = new HashMap<>();
        result.put("result", "success");

        return result;

    }

    //비밀번호 변경
    @PostMapping("/setting/password")
    public Map<String,String> updateMyPageInfoPassword(@RequestBody PwUpdateRequestDto requestDto ,@AuthenticationPrincipal UserDetailsImpl userDetails) throws ContapException {
        userService.updatePassword(requestDto,userDetails.getUsername());

        Map<String,String> result = new HashMap<>();
        result.put("result", "success");

        return result;
    }


    @GetMapping("/auth")
    public Map<String, String> loginCheck(@AuthenticationPrincipal UserDetailsImpl userDetails) throws ContapException {
        if (userDetails == null) {
            throw new ContapException(ErrorCode.LOGIN_TOKEN_EXPIRE);
        }
        Map<String, String> result = new HashMap<>();
        result.put("email", userDetails.getUser().getEmail());
        result.put("userName", userDetails.getUser().getUserName());
        result.put("result", "success");

        return result;
    }


    @Operation(summary = "핸드폰 번호 변경")
    @PostMapping("/setting/modifyPhoneNumber")
    public String modifyPhoneNumber(
            @Parameter(name = "phoneNumber", in = ParameterIn.QUERY, description = "핸드폰 번호") @RequestParam String phoneNumber
            , @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) throws ContapException {
        return userService.modifyPhoneNumber(phoneNumber, userDetails.getUser());
    }

    @Operation(summary = "알림 받기 여부 변경")
    @PostMapping("/setting/alarm")
    public void modifyPhoneNumber(
            @Parameter(name = "alarmState", in = ParameterIn.QUERY, description = "알람 받기 여부(0:취소, 1:받기)")@RequestParam int alarmState
            , @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) throws ContapException {
        userService.changeAlarmState(alarmState, userDetails.getUser());
    }
}
