package com.project.contap.service;

import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import com.project.contap.model.user.EmailRepository;
import com.project.contap.model.user.UserRepository;
import com.project.contap.model.user.dto.EmailRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;

import static com.project.contap.common.util.RandomNumberGeneration.makeRandomNumber;

@Slf4j
@RequiredArgsConstructor
@Service
public class EmailService {

    private final EmailRepository emailRepository;
    private final JavaMailSender emailSender;
    private final UserRepository userRepository;


    public void sendEmail(String email) throws UnsupportedEncodingException, MessagingException {

        String validatedEmail = email.replaceAll(" ", "");

        if (userRepository.existsByEmail(validatedEmail)) {
            throw new RuntimeException("이미 사용 중인 이메일 입니다.");
        }

        String randomNumber = makeRandomNumber();
        MimeMessage message = createMessage(validatedEmail, randomNumber);
        try{
            emailSender.send(message);

        }catch(MailException es){
            es.printStackTrace();
            throw new IllegalArgumentException();
        }

        emailRepository.createEmailCertification(validatedEmail, randomNumber);
    }


    //인증번호 redis저장 일치 확인
    public void verifyEmail(EmailRequestDto requestDto) throws ContapException {

        String validatedEmail = requestDto.getEmail().replaceAll(" ", "");

        if (isVerify(requestDto)) {
            throw new ContapException(ErrorCode.NOT_EQUAL_NUMBER);
        }

        emailRepository.removeEmailCertification(validatedEmail);
    }

    private boolean isVerify(EmailRequestDto requestDto) {

        String validatedEmail = requestDto.getEmail().replaceAll(" ", "");

        boolean isExistKey = emailRepository.hasKey(validatedEmail);
        String findCtfKey = emailRepository.getEmailCertificationNum(validatedEmail);

        String validatedNumber = requestDto.getCertificationNumber().replaceAll(" ", "");

        return !(isExistKey && findCtfKey.equals(validatedNumber));
    }


    //회원가입 인증번호 보내기
    private MimeMessage createMessage(String email, String randomNumber) throws MessagingException, UnsupportedEncodingException {

        MimeMessage message = emailSender.createMimeMessage();

        message.addRecipients(MimeMessage.RecipientType.TO, email);

        message.setSubject("[ConTap] 환영합니다. ConTap 회원 가입 인증번호가 발송되었습니다.");

        String msg="";
        msg += "<img width=\"500\" height=\"300\" style=\"margin-top: 0; margin-right: 0; margin-bottom: 32px; margin-left: 0px; padding-right: 30px; padding-left: 30px;\" src=\"https://media.vlpt.us/images/junseokoo/post/c78c6c89-3b0c-403f-8776-0c39a7303816/%EC%9E%90%EC%82%B0%201@4x-8%201.png\" alt=\"\" loading=\"lazy\">";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">Contap 홈페이지를 방문해주셔서 감사합니다.</h1>";
        msg += "<p style=\"font-size: 14px; padding-right: 30px; padding-left: 30px;\">" +
                "<b>안녕하세요!</b> 회원 가입을 계속 하시려면 해당 인증번호를 인증번호 확인란에 기입해주세요.<br>"+
                "요청한게 본인이 아니라면 발송된 메일을 무시해주세요. " +
                "</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += randomNumber;
        msg += "</td></tr></tbody></table></div>";
        msg += "<p style=\"font-size: 13px; padding-right: 30px; padding-left: 30px;\">" +
                "인증번호 유효시간은 3분입니다."+
                "</p>";
        msg += "<a href=\"https://\" style=\"padding-right: 30px; padding-left: 30px; text-decoration: none; color: #434245;\" rel=\"noreferrer noopener\" target=\"_blank\">2021 Contap</a>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress("ooojunseok@gmail.com","ConTap"));

        return message;
    }


}

