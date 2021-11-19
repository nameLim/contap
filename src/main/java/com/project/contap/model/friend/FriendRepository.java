package com.project.contap.model.friend;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendRepository extends JpaRepository<Friend, Long>,CustomFriendRepository {
    void deleteAllByMe_Id(Long userId);
    void deleteAllByMe_IdOrYou_Id(Long userId);
    void deleteAllByYou_Id(Long userId);
}
