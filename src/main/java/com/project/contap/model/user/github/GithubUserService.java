package com.project.contap.model.user.github;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.contap.model.user.User;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.model.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.UUID;

@Service
public class GithubUserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @Autowired
    public GithubUserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User githubLogin(String code) throws JsonProcessingException {
// 1. "인가 코드"로 "액세스 토큰" 요청
        String accessToken = getAccessToken(code);

// 2. "액세스 토큰"으로 "카카오 사용자 정보" 가져오기
       GithubUserInfoDto GithubUserInfo = getGithubUserInfo(accessToken);

// 3. "카카오 사용자 정보"로 필요시 회원가입  및 이미 같은 이메일이 있으면 기존회원으로 로그인-
        User GithubUser = registerGithubOrUpdateGithub(GithubUserInfo);

// 4. 강제 로그인 처리
        forceLogin(GithubUser);

        return GithubUser;
    }

    private String getAccessToken(String code) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Accept", "application/json");

// HTTP Body 생성
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_secret", "4f0e51148be204784876a50f544598c325a503b9");
        body.add("client_id", "ec87ecda94ea612cbe6c");
        body.add("redirect_uri", "http://localhost:3000/login");
        body.add("code", code);

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> githubTokenRequest =
                new HttpEntity<>(body, headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://github.com/login/oauth/access_token",
                HttpMethod.POST,
                githubTokenRequest,
                String.class
        );

// HTTP 응답 (JSON) -> 액세스 토큰 파싱
        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        return jsonNode.get("access_token").asText();
    }

    private GithubUserInfoDto getGithubUserInfo(String accessToken) throws JsonProcessingException {
// HTTP Header 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

// HTTP 요청 보내기
        HttpEntity<MultiValueMap<String, String>> githubUserInfoRequest = new HttpEntity<>(headers);
        RestTemplate rt = new RestTemplate();
        ResponseEntity<String> response = rt.exchange(
                "https://api.github.com/user",
                HttpMethod.GET,
                githubUserInfoRequest,
                String.class
        );

        String responseBody = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        Long id = jsonNode.get("id").asLong();
        String email = jsonNode.get("login").asText();
        String name = jsonNode.get("name").asText();
        String profile = jsonNode.get("avatar_url").asText();



        return new GithubUserInfoDto(id, email,name,profile);
    }

    private User registerGithubOrUpdateGithub(GithubUserInfoDto githubUserInfo) {
        User sameUser = userRepository.findByEmail(githubUserInfo.getEmail()).orElse(null);

        if (sameUser == null) {
            return registerGithubUserIfNeeded(githubUserInfo);
        } else {
            return updateGithubUser(sameUser, githubUserInfo);
        }
    }

    private User registerGithubUserIfNeeded(GithubUserInfoDto githubUserInfo) {
// DB 에 중복된 github Id 가 있는지 확인
        Long githubId = githubUserInfo.getId();
        User githubUser = userRepository.findByGithubId(githubId)
                .orElse(null);
        if (githubUser == null) {

            String name = githubUserInfo.getName();

// password: random UUID
            String password = UUID.randomUUID().toString();
            String encodedPassword = passwordEncoder.encode(password);


            String email = githubUserInfo.getEmail();
            String profile = githubUserInfo.getProfile();
            githubUser = User.builder()
                    .email(name + "@github.com")
                    .githubId(githubId)
                    .pw(encodedPassword)
                    .userName(name)
                    .profile(profile)
                    .build();
            userRepository.save(githubUser);

        }
        return githubUser;
    }

    private User updateGithubUser(User sameUser, GithubUserInfoDto githubUserInfo) {
        if (sameUser.getGithubId() == null) {
            sameUser.setGithubId(githubUserInfo.getId());
            userRepository.save(sameUser);
        }
        return sameUser;
    }

    private void forceLogin(User githubUser) {
        UserDetails userDetails = new UserDetailsImpl(githubUser);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}