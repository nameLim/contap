package com.project.contap.repository;

import com.project.contap.model.Tap;
import com.project.contap.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TapRepository extends JpaRepository<Tap, Long> {
    Optional<Tap> findBySendUserAndReceiveUser(User send, User reciece);
}
