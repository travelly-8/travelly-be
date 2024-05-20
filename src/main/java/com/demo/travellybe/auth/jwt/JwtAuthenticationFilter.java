package com.demo.travellybe.auth.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        // 토큰 확인
        String accessToken = jwtProvider.resolveAccessToken(request);
        String refreshToken = jwtProvider.resolveRefreshToken(request);

        if(accessToken != null){
            // 토큰 값이 유효하면 검증
            if (jwtProvider.validToken(accessToken)) {

                // 토큰 검증 (인증객체 생성)
                Authentication authentication = jwtProvider.getAuthentication(accessToken);

                // SecurityContext 에 인증 객체 등록
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } else if (!jwtProvider.validToken(accessToken) && refreshToken != null) {

                /// 리프레시 토큰 검증
                boolean validateRefreshToken = jwtProvider.validToken(refreshToken);

                /// 리프레시 토큰 저장소 존재유무 확인
                boolean isRefreshToken = jwtProvider.existsRefreshToken(refreshToken);

                if (validateRefreshToken && isRefreshToken) {

                    /// 리프레시 토큰으로 이메일 정보 가져오기
                    String email = jwtProvider.getUserEmail(refreshToken);

                    /// 토큰 발급
                    String newAccessToken = jwtProvider.generateToken(email, 20 * 60 * 1000L);

                    /// 컨텍스트에 넣기
                    Authentication authentication = jwtProvider.getAuthentication(accessToken);

                    // 헤더에 토큰 설정
                    response.setHeader(HttpHeaders.AUTHORIZATION, newAccessToken);

                    // SecurityContext 에 인증 객체 등록
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
