package com.project.contap.controller;

import com.project.contap.common.enumlist.AlarmEnum;
import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.user.User;
import com.project.contap.model.user.dto.PwUpdateRequestDto;
import com.project.contap.model.user.dto.SignUpRequestDto;
import com.project.contap.model.user.dto.UserRequestDto;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.security.jwt.JwtTokenProvider;
import com.project.contap.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Tag(name = "User Controller Api V1")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(summary = "회원가입")
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


    @Operation(summary = "로그인")
    @PostMapping("/user/login")
    public Map<String, String> login(@RequestBody UserRequestDto requestDto) throws ContapException {
        User user = userService.login(requestDto);

        Map<String, String> result = new HashMap<>();
        result.put("token", jwtTokenProvider.createToken(user.getEmail(), user.getEmail(), user.getUserName())); // "username" : {username}
        result.put("email", user.getEmail());
        result.put("userName", user.getUserName());
        result.put("result", "success");
        String[] alarm = userService.getAlarm(user.getEmail());
        result.put("TAP_RECEIVE", alarm[AlarmEnum.TAP_RECEIVE.getValue()]);
        result.put("REJECT_TAP", alarm[AlarmEnum.REJECT_TAP.getValue()]);
        result.put("ACCEPT_TAP", alarm[AlarmEnum.ACCEPT_TAP.getValue()]);
        result.put("CHAT", alarm[AlarmEnum.CHAT.getValue()]);

        return result;
    }


//    @Operation(summary = "회원탈퇴")
//    @DeleteMapping("/setting/withdrawal")
//    public Map<String, String> deleteUser(@RequestBody PwRequestDto requestDto,@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) throws ContapException {
//
//
//        userService.deleteUser(requestDto,userDetails.getUser());
//
//        Map<String, String> result = new HashMap<>();
//        result.put("result", "success");
//
//        return result;
//
//    }

    @Operation(summary = "회원탈퇴")
    @DeleteMapping("/setting/withdrawal")
    public Map<String, String> deleteUser(@RequestParam String password, @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) throws ContapException {

//        userService.deleteUser(requestDto,userDetails.getUser());

        userService.deleteUser(password,userDetails.getUser());

        Map<String, String> result = new HashMap<>();
        result.put("result", "success");

        return result;

    }

    @Operation(summary = "비밀번호 변경")
    @PostMapping("/setting/password")
    public Map<String,String> updateMyPageInfoPassword(@RequestBody PwUpdateRequestDto requestDto ,@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) throws ContapException {
        userService.updatePassword(requestDto,userDetails.getUsername());

        Map<String,String> result = new HashMap<>();
        result.put("result", "success");

        return result;
    }

    @Operation(summary = "토큰만료")
    @GetMapping("/auth")
    public Map<String, String> loginCheck(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) throws ContapException {
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
