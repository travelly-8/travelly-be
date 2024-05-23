package com.demo.travellybe.auth.controller;

import com.demo.travellybe.auth.dto.*;
import com.demo.travellybe.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login/{registrationId}")
    public ResponseEntity<MemberTokenDto> googleLogin(@RequestBody SocialRequestDto socialRequestDto, @PathVariable("registrationId") String registrationId) {
        MemberTokenDto memberTokenDto = authService.socialLogin(socialRequestDto.getCode(), registrationId);

        return ResponseEntity.ok().body(memberTokenDto);
    }

    @PostMapping("/login")
    public ResponseEntity<MemberTokenDto> formLogin(@RequestBody FormRequestDto formRequestDto) {
        MemberTokenDto memberTokenDto = authService.formLogin(formRequestDto);

        return ResponseEntity.ok().body(memberTokenDto);
    }

    @PostMapping("/login/reissue")
    public ResponseEntity<TokenResponseDto> refreshToken(@RequestBody TokenRequestDto tokenRequestDto) {
        TokenResponseDto tokenResponseDto = authService.reissueToken(tokenRequestDto);

        return ResponseEntity.ok().body(tokenResponseDto);
    }

    @PostMapping("/signup")
    public ResponseEntity<Void> register(@RequestBody @Valid SignupRequestDto signupRequestDto) {
        authService.signup(signupRequestDto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
