package com.project.contap.repository;

import com.project.contap.model.Card;
import com.project.contap.model.HashTag;
import com.project.contap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {
    List<Card> findAllByUser(Long user);


}
