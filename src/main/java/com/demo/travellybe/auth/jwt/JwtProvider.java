package com.demo.travellybe.auth.jwt;

import com.demo.travellybe.auth.domain.RefreshToken;
import com.demo.travellybe.auth.domain.TokenRepository;
import com.demo.travellybe.auth.domain.MemberTokens;
import com.demo.travellybe.auth.service.PrincipalService;
import io.jsonwebtoken.*;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtProvider {

    private String secretKey = "tokensecretkeytokensecretkeytokensecretkeytokensecretkeytokensecretkey";
    private final PrincipalService principalService;
    private final TokenRepository tokenRepository;
    // 어세스 토큰 유효시간 | 20m
    private final long accessTokenValidTime = 20 * 60 * 1000L;
    // 리프레시 토큰 유효시간 | 1h
    private final long refreshTokenValidTime = 60 * 60 * 1000L;

    @PostConstruct
    protected void init() {
        secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
    }
    public MemberTokens generateLoginToken(String subject) {
        String refreshToken = generateToken(subject, refreshTokenValidTime);
        String accessToken = generateToken(subject, accessTokenValidTime);

        // 리프레스 토큰 저장
        tokenRepository.save(new RefreshToken(refreshToken));

        return new MemberTokens(refreshToken, accessToken);
    }

    public String generateToken(String subject, Long validTime) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + validTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    public boolean validToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException | IllegalArgumentException e) {
            return false;
        }
    }

    public Authentication getAuthentication(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();

        UserDetails userDetails = principalService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(userDetails, token, userDetails.getAuthorities());
    }

    public String resolveAccessToken(HttpServletRequest request) {
        if(request.getHeader("authorization") != null )
            return request.getHeader("authorization").substring(7);
        return null;
    }

    public String resolveRefreshToken(HttpServletRequest request) {
        if(request.getHeader("refreshToken") != null )
            return request.getHeader("refreshToken").substring(7);
        return null;
    }

    // RefreshToken 존재유무 확인
    public boolean existsRefreshToken(String refreshToken) {
        return tokenRepository.existsByRefreshToken(refreshToken);
    }

    // 토큰에서 회원 정보 추출
    public String getUserEmail(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }
}
