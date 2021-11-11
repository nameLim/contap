package com.project.contap.controller;

import com.project.contap.common.DefaultRsp;
import com.project.contap.model.card.dto.QCardDto;
import com.project.contap.common.SearchRequestDto;
import com.project.contap.model.user.dto.UserRequestDto;
import com.project.contap.exception.ContapException;
import com.project.contap.model.hashtag.HashTag;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.service.MainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Tag(name = "Main Controller Api V1")
@RestController
public class MainController {
    private final MainService mainService;

    @Autowired
    public MainController(
            MainService mainService
            )
    {
        this.mainService = mainService;
    }

    @Operation(summary = "HashTag")
    @GetMapping("/main/hashtag")
    public List<HashTag> getHashag() throws ContapException {
        return mainService.getHashTag();
    }


    @PostMapping("/main/search") //@RequestBody List<HashTag> hashTags
    public List<UserRequestDto> search(
            @RequestBody SearchRequestDto tagsandtype
            ) throws ContapException {
        return mainService.searchuser(tagsandtype);
    }

    @Operation(summary = "뒷면카드 조회")
    @GetMapping("/main/{userId}")
    public List<QCardDto> getCards(@PathVariable Long userId) throws Exception {
        return mainService.getCards(userId);
    }

    @Operation(summary = "앞면카드 조회")
    @GetMapping("/main")
    public Map<String, Object> getUserDtoList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> result = new HashMap<>();
        List<UserRequestDto> users = mainService.getUserDtoList(userDetails);
        result.put("users", users);
        return result;
    }
    @PostMapping("/main/posttap")
    public DefaultRsp tap(
            @RequestBody(required = false)  UserRequestDto userid,
            @AuthenticationPrincipal UserDetailsImpl userDetails

    ) {
        return mainService.dotap(userDetails.getUser(),userid.getUserId());
    }

    @PostMapping("/main/tutorial")
    public void phoneTutorial(
            @Parameter(name = "tutorialNum", in = ParameterIn.QUERY, description = "튜터리얼 번호(0:핸드폰,1:프로필)") @RequestParam int tutorialNum
            , @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        mainService.tutorial(tutorialNum, userDetails.getUser());
    }
}
