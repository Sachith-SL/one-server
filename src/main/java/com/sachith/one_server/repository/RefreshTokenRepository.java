package com.sachith.one_server.repository;

import com.sachith.one_server.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByToken(String token);
    // check
    void deleteByUserId(Long userId);

    // Optional helper
    void deleteByExpiryDateBefore(Instant now);
}
