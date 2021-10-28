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
//        restApiException.setResult("fail");
//        if(ex.getClass().equals("contaptex"))
//        {
//            ex = (ContapException) ex;
//            restApiException.setHttpStatus(HttpStatus.OK);
//            restApiException.setErrorMessage(ex.gete().getMessage());
//        }
//        else
//        {
//            restApiException.setHttpStatus(HttpStatus.INTERNAL_SERVER_ERROR);
//            restApiException.setErrorMessage("서버에 문의해주세요");
//        }


        return new ResponseEntity(
                restApiException,
                HttpStatus.OK
        );
    }
}

