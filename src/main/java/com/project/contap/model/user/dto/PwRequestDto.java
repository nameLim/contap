package com.project.contap.model.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(name = "Password RequestDto",description = "Password")
public class PwRequestDto {

    @Schema(description = "Passwrod")
    private String pw;


}
