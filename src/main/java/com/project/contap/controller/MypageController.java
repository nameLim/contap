package com.project.contap.controller;

import com.project.contap.dto.*;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/mypage")
@RestController
public class MypageController {
    private final MypageService mypageService;
    //나의 정보
    @GetMapping("/myinfo")
    public UserInfoDto getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.getMyInfo(userDetails.getUser());
    }

    //카드 앞면
    @PostMapping("/frontCard")
    public FrontResponseCardDto modifyFrontCard(
            @ModelAttribute FrontRequestCardDto frontRequestCardDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return mypageService.modifyFrontCard(frontRequestCardDto, userDetails.getUser());
    }

    @PostMapping("/backCard")
    public BackResponseCardDto createBackCard(@RequestBody BackRequestCardDto backRequestCardDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.createBackCard(
                backRequestCardDto,
                userDetails.getUser()
        );
    }

    @PostMapping("/backCard/{cardId}")
    public BackResponseCardDto modifyBackCard(@PathVariable Long cardId, @RequestBody BackRequestCardDto backRequestCardDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.modifyBackCard(cardId, backRequestCardDto, userDetails.getUser());
    }

    @DeleteMapping("/backCard/{cardId}")
    public BackResponseCardDto deleteBackCard(@PathVariable Long cardId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.deleteBackCard(cardId, userDetails.getUser());
    }
}
