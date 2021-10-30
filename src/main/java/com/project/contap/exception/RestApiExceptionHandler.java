package com.project.contap.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleApiRequestException(Exception ex) {
        RestApiException restApiException = new RestApiException();
        restApiException.setResult("fail");
        if (ex instanceof ContapException) {
            restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
            restApiException.setErrorMessage(((ContapException) ex).getErrorCode().getMessage());
        }else {
            restApiException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
            restApiException.setErrorMessage("백엔드 개발자에게 문의해주셈");
        }

        return new ResponseEntity(
                restApiException,
                HttpStatus.OK
        );
    }
}

