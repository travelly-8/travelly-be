package com.demo.travellybe.auth.controller;

import com.demo.travellybe.auth.dto.AuthRequestDto;
import com.demo.travellybe.auth.dto.MemberTokenDto;
import com.demo.travellybe.auth.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping(value="/auth")
public class AuthController {

    private final AuthService authService;
    @PostMapping("/login/{registrationId}")
    public ResponseEntity<MemberTokenDto> googleLogin(@RequestBody AuthRequestDto authRequestDto, @PathVariable("registrationId") String registrationId) {
        MemberTokenDto memberTokenDto = authService.socialLogin(authRequestDto.getCode(), registrationId);

        return ResponseEntity.ok().body(memberTokenDto);
    }
}
