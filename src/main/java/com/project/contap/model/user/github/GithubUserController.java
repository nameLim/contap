package com.project.contap.model.user.github;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.contap.model.user.User;
import com.project.contap.model.user.github.GithubUserService;
import com.project.contap.security.jwt.JwtTokenProvider;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.util.HashMap;
import java.util.Map;

@RestController
public class GithubUserController {

    private final GithubUserService githubUserService;
    private final JwtTokenProvider jwtTokenProvider;

    public GithubUserController(GithubUserService githubUserService, JwtTokenProvider jwtTokenProvider) {
        this.githubUserService = githubUserService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @GetMapping("/user/github")
    public Map<String, String> githubLogin(@RequestParam String code) throws JsonProcessingException {
        User user = githubUserService.githubLogin(code);

        Map<String, String> result = new HashMap<>();
        result.put("token", jwtTokenProvider.createToken(user.getEmail(), user.getEmail(), user.getUserName()));
        result.put("email", user.getEmail());
        result.put("userName", user.getUserName());
        result.put("profile", user.getProfile());
        result.put("result", "success");

        return result;
    }
}