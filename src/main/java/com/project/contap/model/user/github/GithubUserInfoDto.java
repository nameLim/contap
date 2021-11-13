package com.project.contap.model.user.github;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class GithubUserInfoDto {


    private Long id;
    private String name;
    private String email;
    private String profile;

}
