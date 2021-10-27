package com.project.contap.controller;

import com.project.contap.exception.ContapException;
import com.project.contap.model.HashTag;
import com.project.contap.model.User;
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
    @Autowired
    public MainController(HashTagService hashTagService,UserService userService)
    {
        this.hashTagService = hashTagService;
        this.userService= userService;
    }
    @GetMapping("/main/hashtag")
    public List<HashTag> getHashag() throws ContapException {
        return hashTagService.getHashTag();
    }

    @PostMapping("/main/search")
    public List<User> search(
            @RequestBody List<HashTag> hashTags
    ) throws ContapException {
        return userService.getuser(hashTags);
    }




}
