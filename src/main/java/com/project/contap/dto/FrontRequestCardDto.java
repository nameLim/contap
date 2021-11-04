package com.project.contap.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FrontRequestCardDto {
    private MultipartFile profile; // 사용안해서 임시로 바꿔둠
    private String userName;
    private String hashTagsStr;
    private int field;
}
