package com.project.contap.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    CHANGE_PASSWORD_EQUAL(BAD_REQUEST,"변경비밀번호와 현재 비밀번호가 같습니다"),
    CHANGE_EMPTY_PASSWORD(BAD_REQUEST,"변경할 비밀번호를 입력해주세요"),
    CURRNET_EMPTY_PASSWORD(BAD_REQUEST, "현재비밀번호를 입력해주세요."),
    NOT_EQUAL_PASSWORD(BAD_REQUEST, "비밀번호가 일치하지 않습니다."),
    NEW_PASSWORD_NOT_EQUAL(BAD_REQUEST, "새로운 비밀번호와 비밀번호확인이 같지 않습니다"),

    EMAIL_FORM_INVALID(BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
    REGISTER_ERROR(BAD_REQUEST, "회원정보를 입력해주세요."),

    CARD_NOT_FOUND(BAD_REQUEST, "해당 카드를 찾을 수 없습니다."),

    EMAIL_DUPLICATE(BAD_REQUEST, "중복된 email이 존재합니다."),
    USER_NOT_FOUND(BAD_REQUEST, "회원 정보를 찾을 수 없습니다."),

    PASSWORD_PATTERN_LENGTH(BAD_REQUEST, "비밀번호는 6~20자리로 해주세요"),
    PASSWORD_ENTER(BAD_REQUEST, "비밀번호를 입력해주세요"),

    NICKNAME_DUPLICATE(BAD_REQUEST, "중복된 닉네임이 존재합니다."),

    LOGIN_TOKEN_EXPIRE(BAD_REQUEST, "로그인이 만료되었습니다. 재로그인 하세요!");

    private final HttpStatus httpStatus;
    private final String message;
}
