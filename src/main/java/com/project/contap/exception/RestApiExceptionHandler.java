package com.project.contap.exception;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = { ContapException.class })
    public ResponseEntity<Object> handleApiRequestException(ContapException ex) throws Exception{
        //여기서 분기시켜서 우리가 처리한 예외인지 아닌지 판단해야한다.
        // 판단하는 방법은 발생한 예외가 ContapException 인지 확인해야함
        // 처리한 예외가 아니멸 log남기고 적절한값 return
        // 처리한 예외면 해당값 return

        RestApiException restApiException = new RestApiException();
        restApiException.setResult("fail");
        restApiException.setHttpStatus(HttpStatus.OK);
        restApiException.setErrorMessage(ex.getErrorCode().getMessage());

        return new ResponseEntity(
                restApiException,
                HttpStatus.OK
        );
    }
}

