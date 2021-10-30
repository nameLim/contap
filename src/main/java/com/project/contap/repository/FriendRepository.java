package com.project.contap.repository;

import com.project.contap.model.Card;
import com.project.contap.model.Friend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long> {
}
