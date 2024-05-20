package com.demo.travellybe.auth.service;

import com.demo.travellybe.auth.domain.MemberTokens;
import com.demo.travellybe.auth.dto.PrincipalDetails;
import com.demo.travellybe.auth.dto.MemberTokenDto;
import com.demo.travellybe.auth.jwt.JwtProvider;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final Environment env;
    private final WebClient webClient = WebClient.builder().build();
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    // 소셜 로그인
    public MemberTokenDto socialLogin(String code, String registrationId) {

        // 토큰 발급
        String accessToken = getAccessToken(code, registrationId);

        // 토큰 이용해서 유저 정보 발급
        JsonNode userResourceNode = getUserResource(accessToken, registrationId);

        String id = userResourceNode.get("id").asText();
        String email = userResourceNode.get("email").asText();
        String nickname = userResourceNode.get("name").asText();

        // JWT 토큰 반환
        return saveUser(id, email, nickname);
    }

    private String getAccessToken(String authorizationCode, String registrationId) {

        String basePath = "spring.security.oauth2.client.registration.";
        String clientId = env.getProperty(basePath + registrationId + ".client-id");
        String clientSecret = env.getProperty(basePath + registrationId + ".client-secret");
        String redirectUri = env.getProperty(basePath + registrationId + ".redirect-uri");
        String tokenUri = env.getProperty(basePath + registrationId + ".token-uri");

        return webClient.post()
                .uri(tokenUri)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(BodyInserters.fromFormData("code", authorizationCode)
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", redirectUri)
                        .with("grant_type", "authorization_code"))
                .retrieve()
                .bodyToMono(JsonNode.class)
                .map(responseNode -> responseNode.get("access_token").asText())
                .block();
    }

    private JsonNode getUserResource(String accessToken, String registrationId) {
        String resourceUri = env.getProperty("spring.security.oauth2.client.registration."+registrationId+".resource-uri");

        return webClient.get()
                .uri(resourceUri)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(JsonNode.class)
                .block();
    }

    private MemberTokenDto saveUser(String id, String username, String nickname) {
        Optional<Member> memberEntity = memberRepository.findByUsername(username);
        String password = "더미패스워드";
        MemberTokenDto memberTokenDto = new MemberTokenDto();

        if (memberEntity.isEmpty()) {  //  처음 로그인
            Member member = Member.builder()
                    .username(username)
                    .password(password)
                    .nickname(nickname)
                    .build();

            // DB 에 저장
            memberRepository.save(member);

            memberTokenDto.setToken(makeToken(new PrincipalDetails(member)));
            memberTokenDto.setNewUser(true);
        }else{
            memberTokenDto.setToken(makeToken(new PrincipalDetails(memberEntity.get())));
            memberTokenDto.setNewUser(false);
        }

        return memberTokenDto;
    }

    private MemberTokens makeToken(PrincipalDetails authUser) {

        // JWT 토큰 발급
        String name = authUser.getUsername();
        MemberTokens token = jwtProvider.generateLoginToken(name);

        return token;
    }
}
