package com.project.contap.controller;

import com.project.contap.exception.ContapException;
import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.model.User;
import com.project.contap.service.CardService;
import com.project.contap.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


@RestController
public class MainController {
    private final UserService userService;
    private final CardService cardService;

    public MainController(CardService cardService, UserService userService)
    {
        this.cardService = cardService;
        this.userService = userService;
}

    //전체 카드 앞면만 뿌리기
    @GetMapping("/main")
    public Map<String, Object> main(@PathVariable int page, @PageableDefault(page = 0,
            size = 9, sort = "modifiedDt", direction = Sort.Direction.DESC) Pageable pageable) {
        Pageable pageable1 = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
        Page<User> users = userService.main(pageable1);
        Map<String, Object> result = new HashMap<>();
        result.put("users", users);

        return result;
    }


    //카드 뒷면
    @GetMapping("/main/{userId}")
    public Map<String, Object> getCards(@PathVariable Long id) throws ContapException {
        Card card = cardService.getCards(id);

        Map<String, Object> result = new HashMap<>();

        result.put("id", card.getCardId());
        result.put("insertDt", card.getInsertDt());
        result.put("content", card.getContent());
        result.put("title", card.getTitle());

        return result;
    }
}
