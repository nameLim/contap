package com.project.contap.controller;

import com.project.contap.dto.*;
import com.project.contap.exception.ContapException;
import com.project.contap.model.HashTag;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/main/{userId}")
    public List<QCardDto> getCards(@PathVariable Long userId) throws ContapException {
        return mainService.getCards(userId);
    }

    @GetMapping("/main")
    public Map<String, Object> getUserDtoList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> result = new HashMap<>();
        List<UserResponseDto> users = mainService.getUserDtoList(userDetails);
        result.put("users", users);
        return result;
    }
    // 만에하나 유저정보가 null값일 경우를 대비해 예외처리가 필요함! 유저정보중 특히나 이메일 부분!!!우린 널이면 안된다했는데 혹시~적용이 안될수도 있기 때문.

//    //카드 뒷면
//    @GetMapping("/main/{id}")
//    public Map<String, Object> getCards(@PathVariable Long id) throws ContapException {
//        Card card = cardService.getCards(id);
//
//        Map<String, Object> result = new HashMap<>();
//
//        result.put("id", card.getCardId());
//        result.put("insertDt", card.getInsertDt());
//        result.put("content", card.getContent());
//        result.put("title", card.getTitle());
//
//        return result;
//    }
}
