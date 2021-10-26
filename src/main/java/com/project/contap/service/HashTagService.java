package com.project.contap.service;

import com.project.contap.model.HashTag;
import com.project.contap.repository.HashTagRepositoty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HashTagService {
    private final HashTagRepositoty hashTagRepositoty;

    @Autowired
    public HashTagService(HashTagRepositoty hashTagRepositoty)
    {
        this.hashTagRepositoty=hashTagRepositoty;
    }


    public List<HashTag> getHashTag() {
        return hashTagRepositoty.findAll();
    }
}
