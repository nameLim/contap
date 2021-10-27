package com.project.contap.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { ContapException.class })
    public ResponseEntity<Object> handleApiRequestException(ContapException ex) throws Exception{
        RestApiException restApiException = new RestApiException();
        restApiException.setResult("fail");
        restApiException.setHttpStatus(HttpStatus.OK);
        restApiException.setErrorMessage(ex.getErrorCode().getMessage());

        return null;
    }
}

