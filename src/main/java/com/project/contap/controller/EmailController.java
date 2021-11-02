package com.project.contap.controller;

import com.project.contap.dto.EmailRequestDto;
import com.project.contap.exception.ContapException;
import com.project.contap.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class EmailController {

    @ExceptionHandler(IllegalArgumentException.class)
    public String exceptionHandler(Exception e){
        return e.getMessage();
    }

    private final EmailService emailService;


    //인증보내기
    @PostMapping("/email/send")
    public Map<String,String> sendEmail(@RequestBody EmailRequestDto requestDto) throws UnsupportedEncodingException, MessagingException{
        emailService.sendEmail(requestDto.getEmail());
        Map<String, String> result = new HashMap<>();

        result.put("result", "success");
        return result;
    }

    //인증확인
    @PostMapping("/email/confirm")
    public Map<String,String>  emailVerification(@RequestBody EmailRequestDto requestDto) throws ContapException{
        emailService.verifyEmail(requestDto);
        Map<String, String> result = new HashMap<>();
        result.put("result", "success");
        return result;
    }

//    //비번찾기 인증번호
//    @PostMapping("/email/send/reset")
//    public ResponseEntity<String> sendEmailToChangePw(@RequestBody EmailRequestDto requestDto) throws UnsupportedEncodingException, MessagingException {
//        emailService.sendEmailToChangePw(requestDto.getEmail());
//        return ResponseEntity.ok("ok");
//    }
}
