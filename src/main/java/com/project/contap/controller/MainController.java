package com.project.contap.controller;

import com.project.contap.exception.ContapException;
import com.project.contap.model.HashTag;
import com.project.contap.model.User;
import com.project.contap.service.HashTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class MainController {
    private final HashTagService hashTagService;
    @Autowired
    public MainController(HashTagService hashTagService)
    {
        this.hashTagService = hashTagService;
    }
    @GetMapping("/main/hashtag")
    public List<HashTag> getHashag() throws ContapException {
        return hashTagService.getHashTag();
    }


}
