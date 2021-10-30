package com.project.contap.controller;

import com.project.contap.dto.UserFrontCardDto;
import com.project.contap.dto.UserInfoDto;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;


    //나의 정보
    @GetMapping("/myinfo")
    public UserInfoDto getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.getMyInfo(userDetails.getUser());
    }


    //카드 앞면
    @PostMapping("/frontCard")
    public UserFrontCardDto modifyFrontCard(@RequestBody UserFrontCardDto userFrontCardDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.modifyFrontCard(userFrontCardDto, userDetails.getUser());
    }
}
