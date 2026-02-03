package com.sachith.one_server.repository;

import com.sachith.one_server.model.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);
}
