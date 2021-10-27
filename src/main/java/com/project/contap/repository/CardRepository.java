package com.project.contap.repository;

import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
