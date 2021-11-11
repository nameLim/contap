package com.project.contap.model.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>,CostomUserRepository {

    Optional<User> findByEmail(String email);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByUserName(String nickname);

    boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);
}
