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
    @GetMapping("/main/search2") //@RequestBody List<HashTag> hashTags
    public List<UserRequestDto> search2(
    ) throws ContapException {
        return mainService.fortestsearchuser();
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
    @GetMapping("/display/{file}")
    public ResponseEntity<Resource> display(
            @PathVariable String file
    ) {
        String path = "/home/ubuntu/images/"+file; // 이경로는 우분투랑 윈도우랑 다르니까 주의해야댐 우분투 : / 윈도우 \\ 인것같음.
        String folder = "";
        org.springframework.core.io.Resource resource = new FileSystemResource(path);
        if (!resource.exists())
            return new ResponseEntity<org.springframework.core.io.Resource>(HttpStatus.NOT_FOUND);
        HttpHeaders header = new HttpHeaders();
        Path filePath = null;
        try {
            filePath = Paths.get(path);
            header.add("Content-Type", Files.probeContentType(filePath));
        } catch (IOException e) {
            return null;
        }
        return new ResponseEntity<Resource>(resource, header, HttpStatus.OK);
    }
    // 만에하나 유저정보가 null값일 경우를 대비해 예외처리가 필요함! 유저정보중 특히나 이메일 부분!!!우린 널이면 안된다했는데 혹시~적용이 안될수도 있기 때문.

    @PostMapping("/main/tutorial")
    public void phoneTutorial(
            @Parameter(name = "tutorialNum", in = ParameterIn.QUERY, description = "튜터리얼 번호(0:핸드폰,1:프로필)") @RequestParam int tutorialNum
            , @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails) {
        mainService.tutorial(tutorialNum, userDetails.getUser());
    }
}
