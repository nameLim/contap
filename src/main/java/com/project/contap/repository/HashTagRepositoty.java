package com.project.contap.repository;

import com.project.contap.model.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface HashTagRepositoty extends JpaRepository<HashTag, Long> {
    List<HashTag> findAllByNameIn(Set<String> hashName);
    List<HashTag> findAllByNameInOrderByType(Set<String> hashName);
}
