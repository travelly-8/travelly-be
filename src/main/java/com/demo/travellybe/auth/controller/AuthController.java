package com.demo.travellybe.auth.controller;

import com.demo.travellybe.auth.dto.*;
import com.demo.travellybe.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
@Tag(name = "Auth", description = "인증 API")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/{registrationId}")
    @Operation(summary = "소셜 로그인")
    @ApiResponse(responseCode = "200", description = "로그인 성공")
    public ResponseEntity<MemberTokenDto> googleLogin(@RequestBody SocialRequestDto socialRequestDto, @PathVariable("registrationId") String registrationId) {
        MemberTokenDto memberTokenDto = authService.socialLogin(socialRequestDto.getCode(), registrationId);

        return ResponseEntity.ok().body(memberTokenDto);
    }

    @PostMapping("/login")
    @Operation(summary = "자체 로그인")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "로그인 성공"),
            @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다"),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다")
    })
    public ResponseEntity<MemberTokenDto> formLogin(@RequestBody FormRequestDto formRequestDto) {
        MemberTokenDto memberTokenDto = authService.formLogin(formRequestDto);

        return ResponseEntity.ok().body(memberTokenDto);
    }

    @PutMapping("/login/{role}")
    @Operation(summary = "첫 로그인 유저 역할 선택")
    @ApiResponse(responseCode = "200", description = "역할 선택 성공")
    public ResponseEntity<Void> registerRole(@PathVariable("role") String role, @AuthenticationPrincipal PrincipalDetails userInfo) {
        authService.registerRole(userInfo.getUsername(), role);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/login/reissue")
    @Operation(summary = "JWT 재발급 요청")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "재발급 성공"),
            @ApiResponse(responseCode = "401", description = "refreshToken 값이 유효하지 않습니다")
    })
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody TokenRequestDto tokenRequestDto) {
        TokenResponseDto tokenResponseDto = authService.reissueToken(tokenRequestDto);

        return ResponseEntity.ok().body(tokenResponseDto);
    }

    @GetMapping("/login/find")
    @Operation(summary = "닉네임으로 아이디 찾기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "유저 정보 찾기 성공"),
            @ApiResponse(responseCode = "404", description = "해당 회원을 찾을 수 없습니다")
    })
    public ResponseEntity<EmailResponseDto> findEmail(@RequestParam("nickname") String nickname) {

        return ResponseEntity.ok().body(authService.findEmail(nickname));
    }

    @PostMapping("/login/find/password")
    @Operation(summary = "비밀번호 찾기")
    public ResponseEntity<Void> findPassword(@RequestBody PasswordRequestDto passwordRequestDto) {

        authService.findPassword(passwordRequestDto.getNickname(), passwordRequestDto.getEmail());

        return ResponseEntity.ok().build();
    }

    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "회원가입 성공"),
            @ApiResponse(responseCode = "400", description = "1. 이미 존재하는 회원입니다 2. 이미 존재하는 닉네임입니다")
    })
    public ResponseEntity<Void> register(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        authService.signup(signupRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/leave")
    @Operation(summary = "회원 탈퇴")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "회원탈퇴 성공"),
            @ApiResponse(responseCode = "400", description = "비밀번호가 일치하지 않습니다")
    })
    public ResponseEntity<Void> leave(@RequestBody LeaveRequestDto leaveRequestDto,  @AuthenticationPrincipal PrincipalDetails userInfo) {
        authService.leave(leaveRequestDto.getPassword(), userInfo.getPassword(), userInfo.getUsername());

        return ResponseEntity.ok().build();
    }
}
