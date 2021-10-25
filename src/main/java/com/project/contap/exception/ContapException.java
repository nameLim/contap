package com.project.contap.exception;


import lombok.Getter;

@Getter
public class ContapException extends Exception {

    private final ErrorCode errorCode;

    public ContapException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }
}
