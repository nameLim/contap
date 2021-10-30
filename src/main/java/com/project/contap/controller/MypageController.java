package com.project.contap.controller;

import com.project.contap.dto.ProjectAddDto;
import com.project.contap.dto.QUserInfoDto;
import com.project.contap.dto.UserFrontCardDto;
import com.project.contap.dto.UserInfoDto;
import com.project.contap.model.User;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.service.MypageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/mypage")
public class MypageController {

    private final MypageService mypageService;

    @GetMapping("/myinfo")
    public User getMyInfo(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.getMyInfo(userDetails.getUser());
    }

    @PostMapping("/frontCard")
    public UserFrontCardDto modifyFrontCard(
            @RequestBody UserFrontCardDto userFrontCardDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails) throws Exception {
        throw new Exception();
//        return mypageService.modifyFrontCard(userFrontCardDto, userDetails.getUser());
    }

    @PostMapping("/backCard")
    public void addProject(
            @RequestBody ProjectAddDto projectAddDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            HttpServletRequest req,
            HttpServletResponse rsp
    ) throws Exception{
        throw new Exception();
//        mypageService.addProject(projectAddDto, userDetails.getUser());
    }
    @PostMapping("/backCard/{cardId}")
    public void addProject(
            @RequestBody ProjectAddDto projectAddDto,
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId
    ) {
        mypageService.editCard(cardId,projectAddDto, userDetails.getUser());
    }
    @PostMapping("/bakard/{cardId}")
    public void delCard(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long cardId
    ) {
        mypageService.delCard(cardId, userDetails.getUser());
    }
}
