package com.crb.seaturtlewebservice.repository;

import com.crb.seaturtlewebservice.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String name);

    Optional<User> findByEmail(String email);

    List<User> findAllByIsDeletedFalse();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    boolean existsByPhone(String phone);

    Optional<User> findUserByEmailAndVerifyCodeAndVerifyCodeExpirationAfter(
            String email, String verifyCode, LocalDateTime verifyCodeExpiration
    );
}
