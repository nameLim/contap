package com.project.contap.repository;

import com.project.contap.model.HashTag;
import com.project.contap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashTagRepositoty extends JpaRepository<HashTag, Long> {
}
