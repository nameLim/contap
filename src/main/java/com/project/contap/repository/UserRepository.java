package com.project.contap.repository;

import com.project.contap.model.Card;
import com.project.contap.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByUserName(String nickname);

//    Page<User> findAllByOrderByModifiedDtDesc(Pageable pageable);

}
