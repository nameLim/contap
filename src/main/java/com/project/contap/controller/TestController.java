package com.project.contap.controller;

import com.project.contap.dto.SignUpRequestDto;
import com.project.contap.exception.ContapException;
import com.project.contap.model.User;
import com.project.contap.repository.UserRepository;
import com.project.contap.security.jwt.JwtTokenProvider;
import com.project.contap.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {
    private final UserRepository userRepository;
    @Autowired
    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @GetMapping("/test/lsj")
    public void registerUser() throws ContapException {
        User user = userRepository.findById(1L).orElse(null);
        System.out.println(user);
    }
}
