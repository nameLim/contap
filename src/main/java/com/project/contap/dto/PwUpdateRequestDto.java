package com.project.contap.dto;

import com.project.contap.exception.ContapException;
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



        this.currentPw = currentPw;
        this.newPw = newPw;
        this.newPwCheck = newPwCheck;
    }

    public void setNewPw(String newPw){
        this.newPw = newPw;
    }


}
