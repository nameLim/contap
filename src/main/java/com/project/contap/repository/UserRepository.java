package com.project.contap.repository;

import com.project.contap.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByKakaoId(Long kakaoId);

    Optional<User> findByGithubId(Long githubId);

    Optional<User> findByUserName(String nickname);

    boolean existsByEmail(String email);

    Page<User> findAll(Pageable pageable);
    //List<User> findDistinctByTagsIn(List<HashTag> tags);

    @Query("SELECT NEW com.project.contap.model.User(" +
            "u.id,u.userName,u.profile,u.hashTagsString)" +
            " FROM User as u" +
            " WHERE u.id = :id")
    User lsjfind(@Param("id") Long id);


    @Query("SELECT NEW com.project.contap.model.User(" +
            "u.id,u.userName,u.email,u.hashTagsString)" +
            " FROM User as u" +
            " WHERE u.userName = :userName")
    Optional<User> existUserByUserName(@Param("userName") String userName);

    @Query("SELECT NEW com.project.contap.model.User(" +
            "u.id,u.userName,u.email,u.phoneNumber)" +
            " FROM User as u" +
            " WHERE u.phoneNumber = :phoneNumber")
    Optional<User> existUserByPhoneNumber(@Param("phoneNumber") String phoneNumber);
}
