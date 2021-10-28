package com.project.contap.controller;

import com.project.contap.dto.UserRequestDto;
import com.project.contap.exception.ContapException;
import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.model.User;
import com.project.contap.service.CardService;
import com.project.contap.service.HashTagService;
import com.project.contap.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @GetMapping("/main/hashtag")
    public List<HashTag> getHashag() throws ContapException {
        return hashTagService.getHashTag();
    }

    @GetMapping("/main/search") //@RequestBody List<HashTag> hashTags
    public List<UserRequestDto> search(
    ) throws ContapException {
        return userService.getuser(null);
    }

    @GetMapping("/main/{userId}")
    public List<Card> getCards(@PathVariable Long userId) throws ContapException {
        return cardService.getCards(userId);
    }

    @GetMapping("/main")
    public Map<String, Object> getUserDtoList(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        Map<String, Object> result = new HashMap<>();
        List<UserResponseDto> users = userService.getUserDtoList(userDetails);

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
