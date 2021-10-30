package com.project.contap.dto;

import com.project.contap.exception.ContapException;
import com.project.contap.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class PwUpdateRequestDto {

    private String currentPw;
    private String newPw;
    private String newPwCheck;

    public PwUpdateRequestDto(String currentPw, String newPw, String newPwCheck) throws ContapException {

        if(currentPw.isEmpty()){
            throw new ContapException(ErrorCode.CURRNET_EMPTY_PASSWORD);
        }

        if(newPw.isEmpty() || newPw.isEmpty()){
            throw new ContapException(ErrorCode.CHANGE_EMPTY_PASSWORD);
        }

        if(currentPw.equals(newPw)){
            throw new ContapException(ErrorCode.NEW_PASSWORD_NOT_EQUAL);
        }

        if(newPw.length() < 6 || newPw.length() > 20){
            throw new ContapException(ErrorCode.PASSWORD_PATTERN_LENGTH);
        }

        if ( !newPw.equals(newPwCheck)){
            throw new ContapException(ErrorCode.NEW_PASSWORD_NOT_EQUAL);
        }

        this.currentPw = currentPw;
        this.newPw = newPw;
        this.newPwCheck = newPwCheck;
    }

    public void setNewPw(String newPw){
        this.newPw = newPw;
    }


}
