package com.project.contap.controller;

import com.project.contap.dto.UserRequestDto;
import com.project.contap.dto.UserResponseDto;
import com.project.contap.exception.ContapException;
import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.service.CardService;
import com.project.contap.service.HashTagService;
import com.project.contap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MainController {
    private final HashTagService hashTagService;
    private final UserService userService;
    private final CardService cardService;

    @Autowired
    public MainController(HashTagService hashTagService,UserService userService,CardService cardService)
    {
        this.hashTagService = hashTagService;
        this.userService= userService;
        this.cardService = cardService;
    }

    // 해시태그
    @GetMapping("/main/hashtag")
    public List<HashTag> getHashag() throws ContapException {
        return hashTagService.getHashTag();
    }

    //검색
    @GetMapping("/main/search") //@RequestBody List<HashTag> hashTags
    public List<UserRequestDto> search(
    ) throws ContapException {
        return userService.getuser(null);
    }

    //카드 뒷면
    @GetMapping("/main/{userId}")
    public List<Card> getCards(@PathVariable Long userId) throws ContapException {
        return cardService.getCards(userId);
    }

    //카드 앞면
    @GetMapping("/main")
    public Map<String, Object> getUserDtoList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> result = new HashMap<>();
        List<UserResponseDto> users = userService.getUserDtoList(userDetails);
        result.put("users", users);
        return result;
    }



}
