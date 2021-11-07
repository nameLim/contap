package com.project.contap.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.contap.model.User;
import com.project.contap.security.jwt.JwtTokenProvider;
import com.project.contap.service.GithubUserService;
import com.project.contap.service.KakaoUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserKakaoController {

    private final KakaoUserService kakaoUserService;
    private final JwtTokenProvider jwtTokenProvider;
    private final GithubUserService githubUserService;

    public UserKakaoController(KakaoUserService kakaoUserService, JwtTokenProvider jwtTokenProvider, GithubUserService githubUserService) {
        this.kakaoUserService = kakaoUserService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.githubUserService = githubUserService;
    }

    //카카오
    @GetMapping("/user/kakao")
    public Map<String,String> kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        User user = kakaoUserService.kakaoLogin(code);

        Map<String,String> result =new HashMap<>();
        result.put("token",jwtTokenProvider.createToken(user.getEmail(), user.getEmail(), user.getUserName()));
        result.put("email", user.getEmail());
        result.put("userName", user.getUserName());
        result.put("result", "success");

        return result;
    }





}