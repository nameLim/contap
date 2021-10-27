package com.project.contap.controller;

import com.project.contap.exception.ContapException;
import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.service.CardService;
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

    private final CardService cardService;

    public MainController(CardService cardService)
    {
        this.cardService = cardService;
    }

//    //전체 카드 앞면만 뿌리기
//    @GetMapping("/main/frontview")
//    public Map<String, Object> main(@PathVariable int page, @PageableDefault(page = 0,
//            size = 12, sort = "modifiedDt", direction = Sort.Direction.DESC) Pageable pageable) {
//        Pageable pageable1 = PageRequest.of(page, pageable.getPageSize(), pageable.getSort());
//        Page<Card> cards = cardService.main(pageable1);
//        Map<String, Object> result = new HashMap<>();
//        result.put("cards", cards);
//
//        return result;
//    }
//
//
//    //카드 뒷면
//    @GetMapping("/main/backview/{userId}")
//    public Map<String, Object> getCards(@PathVariable Long id) throws ContapException {
//        Card card = cardService.getCards(id);
//
//        Map<String, Object> result = new HashMap<>();
//
//        result.put("id", card.getCardId());
//        result.put("image", card.getImageUrl());
//        result.put("insertDt", card.getInsertDt());
//        result.put("content", card.getContent());
//
//        return result;
//    }
}
