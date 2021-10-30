package com.project.contap.controller;

import com.project.contap.dto.TagDto;
import com.project.contap.dto.UserInfoDto;
import com.project.contap.dto.UserRequestDto;
import com.project.contap.security.UserDetailsImpl;
import com.project.contap.service.ContapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ContapController {
    private final ContapService contapService;
    @Autowired
    public ContapController(ContapService contapService)
    {
        this.contapService= contapService;
    }

    @GetMapping("/contap/dotap")
    public List<UserRequestDto> getMydoTap(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return contapService.getMydoTap(userDetails.getUser());
    }
    @GetMapping("/contap/gettap")
    public List<UserRequestDto> getMyTap(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return contapService.getMyTap(userDetails.getUser());
    }
    @GetMapping("/contap/getothers")
    public List<UserRequestDto> getMyfriends(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return contapService.getMyfriends(userDetails.getUser());
    }

    @PostMapping("/contap/reject")
    public void tapreject(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody(required = false) TagDto tagId) {
        contapService.tapReject(tagId.getTagId());
    }
    @PostMapping("/contap/accept")
    public void acreject(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody(required = false)  TagDto tagId) {
        contapService.rapAccept(tagId.getTagId());
    }

}
