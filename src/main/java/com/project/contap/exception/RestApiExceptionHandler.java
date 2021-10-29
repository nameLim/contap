package com.project.contap.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {
    @ExceptionHandler(value = { Exception.class })
    public ResponseEntity<Object> handleApiRequestException(Exception ex) throws Exception{
        RestApiException restApiException = new RestApiException();
        restApiException.setResult("fail");
        restApiException.setHttpStatus(HttpStatus.BAD_REQUEST);
        restApiException.setErrorMessage(ex.getMessage());


        return new ResponseEntity(
                restApiException,
                HttpStatus.OK
        );
    }
}

