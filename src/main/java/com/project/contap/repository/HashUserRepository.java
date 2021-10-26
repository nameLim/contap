package com.project.contap.repository;
import com.project.contap.model.HashUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashUserRepository extends JpaRepository<HashUser, Long> {
}
