package com.project.contap.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class EmailRequestDto {

    private String email;
    private String certificationNumber;

    public EmailRequestDto(String email, String certificationNumber) {
        this.email = email;
        this.certificationNumber = certificationNumber;
    }
}

