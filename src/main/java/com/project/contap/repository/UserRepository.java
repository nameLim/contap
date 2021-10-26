package com.project.contap.repository;

import com.project.contap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByUserName(String nickname);

    @Query("SELECT NEW com.project.contap.model.User(" +
            "u.id,u.email,u.pw,u.userName,u.kakaoId,u.profile)" +
            " FROM User as u" +
            " WHERE u.id = :id")
    User lsjfind(@Param("id") Long id);
    // 데이터 베이스 조회 속도 느릴때  이런식으로 쿼리문 작성해서 최적화를 해주세요.
    // 위의 함수는 Tag랑Card를 조회 하지 않는답니다~~

}
