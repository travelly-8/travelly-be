package com.demo.travellybe.auth.service;

import com.demo.travellybe.auth.domain.MemberTokens;
import com.demo.travellybe.auth.dto.*;
import com.demo.travellybe.auth.jwt.JwtProvider;
import com.demo.travellybe.exception.CustomException;
import com.demo.travellybe.exception.ErrorCode;
import com.demo.travellybe.member.domain.Member;
import com.demo.travellybe.member.domain.MemberRepository;
import com.demo.travellybe.member.domain.Role;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collection;
import java.util.Optional;
@Service
@Transactional
@RequiredArgsConstructor
public class AuthService {

    private final Environment env;
    private final WebClient webClient = WebClient.builder().build();
    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원 가입
    public void signup(SignupRequestDto signupRequestDto) {

        if (memberRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.MEMBER_EMAIL_DUPLICATION);
        }

        if (memberRepository.findByNickname(signupRequestDto.getNickname()).isPresent()) {
            throw new CustomException(ErrorCode.MEMBER_NICKNAME_DUPLICATION);
        }

        // 비밀번호 암호화
        String password = bCryptPasswordEncoder.encode(signupRequestDto.getPassword());
        signupRequestDto.setPassword(password);
        signupRequestDto.setType("local");

        memberRepository.save(signupRequestDto.toEntity());
    }

    // 자체 로그인
    public MemberTokenDto formLogin(FormRequestDto loginData) {

        String email = loginData.getEmail();
        String password = loginData.getPassword();

        if (memberRepository.findByEmail(email).isEmpty()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        MemberTokenDto memberTokenDto = new MemberTokenDto();

        // 사용자 인증을 시도하기 위한 토큰 생성(아직 인증이 되지 않은 상태)
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);

        try{
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // 인증 정보를 기반으로 JWT 토큰 생성
            MemberTokens token = jwtProvider.generateLoginToken(authentication.getName());

            memberTokenDto.setToken(token);

            memberRepository.findNicknameByEmail(authentication.getName()).ifPresent(memberTokenDto::setNickname);
            memberRepository.findRoleByEmail(authentication.getName()).ifPresent(role -> memberTokenDto.setRole(role.toString().toLowerCase()));

        }catch (BadCredentialsException e) {
            throw new CustomException(ErrorCode.MEMBER_PASSWORD_MISMATCH);
        }

        return memberTokenDto;
    }


    // 소셜 로그인
    public MemberTokenDto socialLogin(String code, String registrationId) {

        // 토큰 발급
        String accessToken = getAccessToken(code, registrationId);

        // 토큰 이용해서 유저 정보 발급
        JsonNode userResourceNode = getUserResource(accessToken, registrationId);

        if (registrationId.equals("naver")) {
            userResourceNode = userResourceNode.get("response");
        }

        String email = userResourceNode.get("email").asText();
        String nickname = registrationId + "_" + userResourceNode.get("name").asText();

        // JWT 토큰 반환
        return socialUser(email, nickname);
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

    private MemberTokenDto socialUser(String email, String nickname) {
        Optional<Member> memberEntity = memberRepository.findByEmail(email);
        String password = "더미패스워드";
        MemberTokenDto memberTokenDto = new MemberTokenDto();

        if (memberEntity.isEmpty()) {  //  처음 로그인
            Member member = Member.builder()
                    .email(email)
                    .password(password)
                    .nickname(nickname)
                    .type("social")
                    .build();

            // DB 에 저장
            memberRepository.save(member);

            memberTokenDto.setToken(makeToken(new PrincipalDetails(member)));
            memberTokenDto.setNickname(nickname);
        }else{
            memberTokenDto.setToken(makeToken(new PrincipalDetails(memberEntity.get())));
            memberTokenDto.setNickname(memberEntity.get().getNickname());
            memberTokenDto.setRole(memberEntity.get().getRole().toString().toLowerCase());
        }

        return memberTokenDto;
    }

    private MemberTokens makeToken(PrincipalDetails authUser) {

        // JWT 토큰 발급
        String name = authUser.getUsername();
        MemberTokens token = jwtProvider.generateLoginToken(name);

        return token;
    }

    public TokenResponseDto reissueToken(TokenRequestDto tokenRequestDto) {
        String refreshToken = tokenRequestDto.getRefreshToken();

        // refresh 토큰 검증
        boolean validateRefreshToken = jwtProvider.validToken(refreshToken);

        // refresh 토큰 저장소 존재유무 확인
        boolean isRefreshToken = jwtProvider.existsRefreshToken(refreshToken);

        if (validateRefreshToken && isRefreshToken) {

            // 리프레시 토큰으로 이메일 정보 가져오기
            String email = jwtProvider.getUserEmail(refreshToken);

            // 토큰 발급
            String newAccessToken = jwtProvider.generateToken(email, 20 * 60 * 1000L);

            // 컨텍스트에 넣기
            Authentication authentication = jwtProvider.getAuthentication(newAccessToken);

            // SecurityContext 에 인증 객체 등록
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 새로운 access 토큰 반환
            return new TokenResponseDto(newAccessToken);
        }else{
            // refreshToken 도 유효하지 않을 경우
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }
    }

    public void registerRole(String email, String role) {
        // 사용자를 찾고 권한을 설정
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member.setRole(Role.valueOf(role.toUpperCase()));
        memberRepository.save(member);

        // 현재 인증된 사용자 정보
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // 새로운 권한 설정
        Collection<? extends GrantedAuthority> updatedAuthorities = AuthorityUtils.createAuthorityList(role.toUpperCase());

        // 새로운 인증 객체 생성
        Authentication newAuth = new UsernamePasswordAuthenticationToken(authentication.getPrincipal(), authentication.getCredentials(), updatedAuthorities);

        // SecurityContext에 새로운 인증 객체 설정
        SecurityContextHolder.getContext().setAuthentication(newAuth);
    }

    public String findEmail(String nickname) {
        return memberRepository.findByNickname(nickname).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND)).getEmail();
    }

    public void leave(String password, String userPassword, String email) {

        // 비밀번호 일치 여부 확인
        if (bCryptPasswordEncoder.matches(password, userPassword)) {
            memberRepository.deleteByEmail(email);
        }else{
            throw new CustomException(ErrorCode.MEMBER_PASSWORD_MISMATCH);
        }
    }
}
