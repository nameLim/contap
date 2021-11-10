package com.project.contap.controller;

import com.project.contap.model.card.dto.BackRequestCardDto;
import com.project.contap.model.card.dto.BackResponseCardDto;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.service.MypageService;
import com.project.contap.model.user.dto.FrontRequestCardDto;
import com.project.contap.model.user.dto.FrontResponseCardDto;
import com.project.contap.model.user.dto.UserInfoDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/mypage")
@RestController
@Tag(name = "MyPage Controller Api V1")
public class MypageController {
    private final MypageService mypageService;
    
    @Operation(summary = "내 정보 조회")
    @GetMapping("/myinfo")
    public UserInfoDto getMyInfo(@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.getMyInfo(userDetails.getUser());
    }

    @Operation(summary = "카드 앞면 수정")
    @PostMapping("/frontCard")
    public FrontResponseCardDto modifyFrontCard(
            @ModelAttribute FrontRequestCardDto frontRequestCardDto
            ,@Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) throws IOException {
        return mypageService.modifyFrontCard(frontRequestCardDto, userDetails.getUser());
    }

    @Operation(summary = "카드 뒷면 생성")
    @PostMapping("/backCard")
    public BackResponseCardDto createBackCard(
            @RequestBody BackRequestCardDto backRequestCardDto
            , @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.createBackCard(
                backRequestCardDto,
                userDetails.getUser()
        );
    }

    @Operation(summary = "카드 뒷면 수정")
    @PostMapping("/backCard/{cardId}")
    public BackResponseCardDto modifyBackCard(
            @Parameter(name = "cardId", in = ParameterIn.PATH, description = "카드아이디") @PathVariable Long cardId
            , @RequestBody BackRequestCardDto backRequestCardDto
            , @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.modifyBackCard(cardId, backRequestCardDto, userDetails.getUser());
    }

    @Operation(summary = "카드 뒷면 삭제")
    @DeleteMapping("/backCard/{cardId}")
    public BackResponseCardDto deleteBackCard(
            @Parameter(name = "cardId", in = ParameterIn.PATH, description = "카드아이디") @PathVariable Long cardId
            , @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return mypageService.deleteBackCard(cardId, userDetails.getUser());
    }
}
