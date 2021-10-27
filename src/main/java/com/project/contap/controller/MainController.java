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
}
