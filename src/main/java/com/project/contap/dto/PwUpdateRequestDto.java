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

    public PwUpdateRequestDto(String currentPassword, String newPassword, String newPasswordCheck) throws ContapException {

        if(currentPassword.isEmpty()){
            throw new ContapException(ErrorCode.CURRNET_EMPTY_PASSWORD);
        }

        if(newPassword.isEmpty() || newPasswordCheck.isEmpty()){
            throw new ContapException(ErrorCode.CHANGE_EMPTY_PASSWORD);
        }

        if(currentPassword.equals(newPassword)){
            throw new ContapException(ErrorCode.NEW_PASSWORD_NOT_EQUAL);
        }

        if(newPassword.length() < 6 || newPassword.length() > 20){
            throw new ContapException(ErrorCode.PASSWORD_PATTERN_LENGTH);
        }

        if ( !newPassword.equals(newPasswordCheck)){
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
