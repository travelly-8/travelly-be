package com.demo.travellybe.auth.domain;

import com.demo.travellybe.auth.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<RefreshToken, String> {
    boolean existsByRefreshToken(String token);
}
