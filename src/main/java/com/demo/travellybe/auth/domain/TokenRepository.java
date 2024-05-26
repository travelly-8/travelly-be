package com.demo.travellybe.auth.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<RefreshToken, String> {
    boolean existsByRefreshToken(String token);
}
